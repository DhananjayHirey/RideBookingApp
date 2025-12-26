package com.rideApp.BFF.grpc.client;

import com.rideApp.BFF.domain.User;
import com.rideApp.BFF.user.GetUserRequest;
import com.rideApp.BFF.user.UserResponse;
import com.rideApp.BFF.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
public class UserGrpcClient {

    private User map(UserResponse response) {
        return User.builder()
                .id(response.getId())
                .name(response.getName())
                .email(response.getEmail())
                .build();
    }


    private final UserServiceGrpc.UserServiceStub userStub;

    public Mono<User> getUser(String userId) {

        return Mono.create(sink ->
                userStub.getUser(
                        GetUserRequest.newBuilder()
                                .setUserId(userId)
                                .build(),
                        new StreamObserver<>() {

                            @Override
                            public void onNext(UserResponse value) {
                                sink.success(map(value));
                            }

                            @Override
                            public void onError(Throwable t) {
                                sink.error(t);
                            }

                            @Override
                            public void onCompleted() {}
                        }
                )
        );
    }
}
