package com.rideApp.BFF.grpc.client;

import com.rideApp.BFF.domain.Order;
import reactor.core.publisher.Mono;

import java.util.List;

public class OrderGrpcClient {
    public Mono<List<Order>> getRecentOrders(String userId) {
        return null;
    }
}
