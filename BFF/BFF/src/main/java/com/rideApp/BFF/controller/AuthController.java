package com.rideApp.BFF.controller;

import com.rideApp.AuthService.LoginRequest;
import com.rideApp.AuthService.RefreshRequest;
import com.rideApp.AuthService.RegisterRequest;
import com.rideApp.AuthService.AuthResponse;
import com.rideApp.BFF.dto.AuthResponseDto;
import com.rideApp.BFF.grpc.client.AuthGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthGrpcClient authGrpcClient;

    @PostMapping("/register")
    public Mono<AuthResponseDto> register(@RequestBody RegisterDto registerDto) {
        RegisterRequest request = RegisterRequest.newBuilder()
                .setPhoneNumber(registerDto.phoneNumber())
                .setEmail(registerDto.email())
                .setPassword(registerDto.password())
                .setRole(registerDto.role())
                .build();
        return authGrpcClient.register(request);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        LoginRequest request = LoginRequest.newBuilder()
                .setPhoneNumber(loginDto.phoneNumber())
                .setPassword(loginDto.password())
                .build();
        return authGrpcClient.login(request);
    }

    @PostMapping("/refresh")
    public Mono<AuthResponseDto> refresh(@RequestBody RefreshDto refreshDto) {
        RefreshRequest request = RefreshRequest.newBuilder()
                .setRefreshToken(refreshDto.refreshToken())
                .build();
        return authGrpcClient.refreshToken(request);
    }

    // DTOs
    public record RegisterDto(String phoneNumber, String email, String password, String role) {
    }

    public record LoginDto(String phoneNumber, String password) {
    }

    public record RefreshDto(String refreshToken) {
    }
}
