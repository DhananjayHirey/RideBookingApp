package com.rideApp.BFF.application;

import com.rideApp.BFF.domain.Notification;
import com.rideApp.BFF.domain.Order;
import com.rideApp.BFF.domain.User;
import com.rideApp.BFF.dto.DashboardResponse;
import com.rideApp.BFF.grpc.client.NotificationGrpcClient;
import com.rideApp.BFF.grpc.client.OrderGrpcClient;
import com.rideApp.BFF.grpc.client.UserGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserGrpcClient userGrpcClient;
    private final OrderGrpcClient orderGrpcClient;
    private final NotificationGrpcClient notificationGrpcClient;

    public Mono<DashboardResponse> getDashboard(String userId) {

        Mono<User> userMono = userGrpcClient.getUser(userId);
        Mono<List<Order>> ordersMono = orderGrpcClient.getRecentOrders(userId);
        Mono<List<Notification>> notificationsMono =
                notificationGrpcClient.getNotifications(userId);

        return Mono.zip(userMono, ordersMono, notificationsMono)
                .map(tuple -> new DashboardResponse(
                        tuple.getT1(),
                        tuple.getT2(),
                        tuple.getT3()
                ));
    }
}
