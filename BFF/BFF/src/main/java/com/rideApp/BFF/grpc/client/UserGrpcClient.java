package com.rideApp.BFF.grpc.client;

import com.rideApp.UserService.GetProfileRequest;
import com.rideApp.UserService.RiderProfile;
import com.rideApp.UserService.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import com.rideApp.UserService.*;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.MonoSink;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserGrpcClient {

    private final UserServiceGrpc.UserServiceStub userServiceStub;

    // ---------------- GET RIDER ----------------

    public Mono<RiderProfile> getRiderProfile() {
        return getUserId()
                .flatMap(userId ->
                        Mono.create(sink ->
                                userServiceStub.getRiderProfile(
                                        GetProfileRequest.newBuilder()
                                                .setUserId(userId)
                                                .build(),
                                        new StreamObserver<>() {
                                            @Override
                                            public void onNext(RiderProfile value) {
                                                sink.success(value);
                                            }

                                            @Override
                                            public void onError(Throwable t) {
                                                sink.error(t);
                                            }

                                            @Override
                                            public void onCompleted() {}
                                        }
                                )
                        )
                );
    }

    // ---------------- GET DRIVER ----------------

    public Mono<DriverProfile> getDriverProfile() {
        return getUserId()
                .flatMap(userId ->
                        Mono.create(sink ->
                                userServiceStub.getDriverProfile(
                                        GetProfileRequest.newBuilder()
                                                .setUserId(userId)
                                                .build(),
                                        new StreamObserver<>() {
                                            @Override
                                            public void onNext(DriverProfile value) {
                                                sink.success(value);
                                            }

                                            @Override
                                            public void onError(Throwable t) {
                                                sink.error(t);
                                            }

                                            @Override
                                            public void onCompleted() {}
                                        }
                                )
                        )
                );
    }

    // ---------------- CREATE RIDER ----------------

    public Mono<Void> createRider(String name) {
        return getUserId()
                .flatMap(userId ->
                        Mono.create(sink ->
                                userServiceStub.createRider(
                                        CreateRiderRequest.newBuilder()
                                                .setUserId(userId)
                                                .setName(name)
                                                .build(),
                                        emptyObserver(sink)
                                )
                        )
                );
    }

    // ---------------- CREATE DRIVER ----------------

    public Mono<Void> createDriver() {
        return getUserId()
                .flatMap(userId ->
                        Mono.create(sink ->
                                userServiceStub.createDriver(
                                        CreateDriverRequest.newBuilder()
                                                .setUserId(userId)
                                                .build(),
                                        emptyObserver(sink)
                                )
                        )
                );
    }

    // ---------------- HELPERS ----------------

    private Mono<String> getUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (JwtAuthenticationToken) Objects.requireNonNull(ctx.getAuthentication()))
                .map(JwtAuthenticationToken::getName);
    }

    private StreamObserver<UserEmpty> emptyObserver(MonoSink<Void> sink) {
        return new StreamObserver<>(){
            @Override public void onNext(UserEmpty value) {}
            @Override public void onError(Throwable t) { sink.error(t); }
            @Override public void onCompleted() { sink.success(); }
        };
    }
}
