package com.rideApp.BFF.grpc.client;

import com.rideApp.BFF.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationGrpcClient {
    public Mono<List<Notification>> getNotifications(String userId) {

        return null;
    }
}
