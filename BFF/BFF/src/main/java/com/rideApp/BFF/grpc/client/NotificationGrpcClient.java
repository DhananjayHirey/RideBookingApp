package com.rideApp.BFF.grpc.client;

import com.rideApp.BFF.domain.Notification;
import reactor.core.publisher.Mono;

import java.util.List;

public class NotificationGrpcClient {
    public Mono<List<Notification>> getNotifications(String userId) {

        return null;
    }
}
