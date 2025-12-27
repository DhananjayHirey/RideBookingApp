package com.rideApp.BFF.grpc.client;

import com.rideApp.BFF.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderGrpcClient {
    public Mono<List<Order>> getRecentOrders(String userId) {
        return null;
    }
}
