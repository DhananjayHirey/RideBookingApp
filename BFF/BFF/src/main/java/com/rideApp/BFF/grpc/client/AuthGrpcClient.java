package com.rideApp.BFF.grpc.client;

import com.rideApp.AuthService.*;
import com.rideApp.BFF.dto.AuthResponseDto;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthGrpcClient {

    private final AuthServiceGrpc.AuthServiceStub authStub;

    public Mono<AuthResponseDto> register(RegisterRequest request) {
        return Mono.create(sink -> authStub.register(request, new StreamObserver<>() {
            @Override
            public void onNext(AuthResponse value) {
                sink.success(new AuthResponseDto(value.getAccessToken(),value.getRefreshToken(),value.getRole()));
            }

            @Override
            public void onError(Throwable t) {
                sink.error(t);
            }

            @Override
            public void onCompleted() {
            }
        }));
    }

    public Mono<AuthResponseDto> login(LoginRequest request) {
        return Mono.create(sink -> authStub.login(request, new StreamObserver<>() {
            @Override
            public void onNext(AuthResponse value) {
                sink.success(new AuthResponseDto(value.getAccessToken(),value.getRefreshToken(),value.getRole()));
            }

            @Override
            public void onError(Throwable t) {
                sink.error(t);
            }

            @Override
            public void onCompleted() {
            }
        }));
    }

    public Mono<AuthResponseDto> refreshToken(RefreshRequest request) {
        return Mono.create(sink -> authStub.refreshToken(request, new StreamObserver<>() {
            @Override
            public void onNext(AuthResponse value) {
                sink.success(new AuthResponseDto(value.getAccessToken(),value.getRefreshToken(),value.getRole()));
            }

            @Override
            public void onError(Throwable t) {
                sink.error(t);
            }

            @Override
            public void onCompleted() {
            }
        }));
    }
}
