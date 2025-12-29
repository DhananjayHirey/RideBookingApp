package com.rideApp.AuthService.grpc;

import com.rideApp.AuthService.*;
import com.rideApp.AuthService.entity.Role;
import com.rideApp.AuthService.entity.User;
import com.rideApp.AuthService.repository.UserRepository;
import com.rideApp.AuthService.service.JwtService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@GrpcService
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthGrpcService(UserRepository userRepo, JwtService jwtService, RedisTemplate<String, String> redisTemplate) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void login(LoginRequest request,
                      StreamObserver<AuthResponse> responseObserver) {

        User user = userRepo.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

//        System.out.println(user.getPasswordHash());
//        System.out.println(request.getPassword());
        if (!encoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = UUID.randomUUID().toString();

        redisTemplate.opsForValue()
                .set("refresh:" + refreshToken, user.getId().toString(), 7, TimeUnit.DAYS);

        AuthResponse response = AuthResponse.newBuilder()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setRole(user.getRole().name())
                .setUserId(user.getId().toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void refreshToken(RefreshRequest request,
                             StreamObserver<AuthResponse> observer) {

        String key = "refresh:" + request.getRefreshToken();
        String userId = redisTemplate.opsForValue().get(key);

        if (userId == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        User user = userRepo.findById(UUID.fromString(userId)).orElseThrow();

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = UUID.randomUUID().toString();

        redisTemplate.delete(key);
        redisTemplate.opsForValue()
                .set("refresh:" + newRefreshToken, userId, 7, TimeUnit.DAYS);

        observer.onNext(AuthResponse.newBuilder()
                .setAccessToken(newAccessToken)
                .setRefreshToken(newRefreshToken)
                .setRole(user.getRole().name())
                .build());

        observer.onCompleted();
    }

    @Override
    public void register(RegisterRequest request,
                         StreamObserver<AuthResponse> observer) {

        try {
            if (request.getPhoneNumber().isBlank() || request.getPassword().isBlank()) {
                observer.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Phone and password required")
                                .asRuntimeException()
                );
                return;
            }

            if (userRepo.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
                observer.onError(
                        Status.ALREADY_EXISTS
                                .withDescription("User already exists")
                                .asRuntimeException()
                );
                return;
            }

            Role role;
            try {
                role = Role.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                observer.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("Invalid role")
                                .asRuntimeException()
                );
                return;
            }

            User user = new User();
            user.setId(UUID.randomUUID());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setEmail(request.getEmail());
            user.setPasswordHash(encoder.encode(request.getPassword()));
            user.setRole(role);
            user.setActive(true);

            userRepo.save(user);

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = UUID.randomUUID().toString();

            redisTemplate.opsForValue()
                    .set("refresh:" + refreshToken,
                            user.getId().toString(),
                            7,
                            TimeUnit.DAYS);

            observer.onNext(
                    AuthResponse.newBuilder()
                            .setAccessToken(accessToken)
                            .setRefreshToken(refreshToken)
                            .setRole(role.name())
                            .setUserId(user.getId().toString())
                            .build()
            );

            observer.onCompleted();

        } catch (Exception e) {
            e.printStackTrace(); // 👈 you WILL see the real cause now
            observer.onError(
                    Status.INTERNAL
                            .withDescription("Registration failed")
                            .asRuntimeException()
            );
        }
    }



    @PostConstruct
    public void redisHealthCheck() {
        redisTemplate.opsForValue().set("hello", "redis");
        System.out.println(redisTemplate.opsForValue().get("hello"));
    }
}
