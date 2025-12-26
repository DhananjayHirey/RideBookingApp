package com.rideApp.BFF.grpc.client;

import com.rideApp.BFF.domain.User;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
@RequiredArgsConstructor
public class UserGrpcClient {

//    private final UserServiceGrpc.UserServiceStub userStub;

    public Mono<User> getUser(String userId) {

//        return Mono.deferContextual(ctx -> {
//
//            ServerWebExchange exchange =
//                    ctx.get(ServerWebExchange.class);
//
//            String authHeader = exchange.getRequest()
//                    .getHeaders()
//                    .getFirst(HttpHeaders.AUTHORIZATION);
//
//            Metadata metadata = new Metadata();
//            if (authHeader != null) {
//                metadata.put(
//                        Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER),
//                        authHeader
//                );
//            }
//
//            UserServiceGrpc.UserServiceStub stubWithAuth =
//                    MetadataUtils.attachHeaders(userStub, metadata);
//
//            return Mono.create(sink ->
//                    stubWithAuth.getUser(
//                            GetUserRequest.newBuilder().setUserId(userId).build(),
//                            new StreamObserver<>() {
//                                @Override
//                                public void onNext(UserResponse value) {
//                                    sink.success(map(value));
//                                }
//
//                                @Override
//                                public void onError(Throwable t) {
//                                    sink.error(t);
//                                }
//
//                                @Override
//                                public void onCompleted() {}
//                            }
//                    )
//            );
//        });
        return null;
    }
}
