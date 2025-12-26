package com.rideApp.BFF.config;

import com.rideApp.BFF.grpc.interceptor.GrpcJwtInterceptor;
import com.rideApp.BFF.notification.NotificationServiceGrpc;
import com.rideApp.BFF.order.OrderServiceGrpc;
import com.rideApp.BFF.user.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel userServiceChannel() {
        return ManagedChannelBuilder
                .forAddress("user-service", 9090)
                .usePlaintext()
                .build();
    }

    @Bean
    public ManagedChannel orderServiceChannel() {
        return ManagedChannelBuilder
                .forAddress("order-service", 9091)
                .usePlaintext()
                .build();
    }

    @Bean
    public ManagedChannel notificationServiceChannel() {
        return ManagedChannelBuilder
                .forAddress("notification-service", 9092)
                .usePlaintext()
                .build();
    }

    @Bean
    public UserServiceGrpc.UserServiceStub userServiceStub(
            ManagedChannel userServiceChannel,
            GrpcJwtInterceptor jwtInterceptor
    ) {
        return UserServiceGrpc.newStub(userServiceChannel)
                .withInterceptors(jwtInterceptor);
    }

    @Bean
    public OrderServiceGrpc.OrderServiceStub orderServiceStub(
            ManagedChannel orderServiceChannel,
            GrpcJwtInterceptor jwtInterceptor
    ) {
        return OrderServiceGrpc.newStub(orderServiceChannel)
                .withInterceptors(jwtInterceptor);
    }

    @Bean
    public NotificationServiceGrpc.NotificationServiceStub notificationServiceStub(
            ManagedChannel notificationServiceChannel,
            GrpcJwtInterceptor jwtInterceptor
    ) {
        return NotificationServiceGrpc.newStub(notificationServiceChannel)
                .withInterceptors(jwtInterceptor);
    }

}
