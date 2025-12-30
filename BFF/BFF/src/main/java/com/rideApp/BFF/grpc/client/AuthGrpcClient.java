package com.rideApp.BFF.grpc.client;

import com.rideApp.AuthService.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthGrpcClient {

    private final AuthServiceGrpc.AuthServiceStub authStub;

    public Mono<AuthResponse> register(RegisterRequest request) {
        return Mono.create(sink -> authStub.register(request, new StreamObserver<>() {
            @Override
            public void onNext(AuthResponse value) {
                sink.success(value);
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

    public Mono<AuthResponse> login(LoginRequest request) {
        return Mono.create(sink -> authStub.login(request, new StreamObserver<>() {
            @Override
            public void onNext(AuthResponse value) {
                sink.success(value);
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

    public Mono<AuthResponse> refreshToken(RefreshRequest request) {
        return Mono.create(sink -> authStub.refreshToken(request, new StreamObserver<>() {
            @Override
            public void onNext(AuthResponse value) {
                sink.success(value);
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
