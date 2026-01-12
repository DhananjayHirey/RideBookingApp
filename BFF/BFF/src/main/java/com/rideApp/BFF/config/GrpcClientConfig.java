package com.rideApp.BFF.config;

import com.rideApp.AuthService.AuthServiceGrpc;
import com.rideApp.BFF.notification.NotificationServiceGrpc;
import com.rideApp.BFF.order.OrderServiceGrpc;
import com.rideApp.RideService.RideServiceGrpc;
import com.rideApp.UserService.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

        @Bean
        public ManagedChannel userServiceChannel() {
                return ManagedChannelBuilder
                        .forAddress("localhost", 9091)
                        .usePlaintext()
                        .build();
        }
        @Bean
        public ManagedChannel rideServiceChannel() {
                return ManagedChannelBuilder
                        .forAddress("localhost", 9095)
                        .usePlaintext()
                        .build();
        }

        @Bean
        public ManagedChannel orderServiceChannel() {
                return ManagedChannelBuilder
                        .forAddress("localhost", 9090)
                        .usePlaintext()
                        .build();
        }

        @Bean
        public ManagedChannel notificationServiceChannel() {
                return ManagedChannelBuilder
                        .forAddress("notification-service", 9093)
                        .usePlaintext()
                        .build();
        }

        // ⬇️ NO interceptor here
        @Bean
        public UserServiceGrpc.UserServiceStub userServiceStub(
                ManagedChannel userServiceChannel) {

                return UserServiceGrpc.newStub(userServiceChannel);
        }
        @Bean
        public RideServiceGrpc.RideServiceStub rideServiceStub(
                ManagedChannel rideServiceChannel) {

                return RideServiceGrpc.newStub(rideServiceChannel);
        }

        @Bean
        public OrderServiceGrpc.OrderServiceStub orderServiceStub(
                ManagedChannel orderServiceChannel) {

                return OrderServiceGrpc.newStub(orderServiceChannel);
        }

        @Bean
        public NotificationServiceGrpc.NotificationServiceStub notificationServiceStub(
                ManagedChannel notificationServiceChannel) {

                return NotificationServiceGrpc.newStub(notificationServiceChannel);
        }

        @Bean
        public ManagedChannel authServiceChannel() {
                return ManagedChannelBuilder
                        .forAddress("localhost", 9092)
                        .usePlaintext()
                        .build();
        }

        @Bean
        public AuthServiceGrpc.AuthServiceStub authServiceStub(
                ManagedChannel authServiceChannel) {

                return AuthServiceGrpc.newStub(authServiceChannel);
        }
}
