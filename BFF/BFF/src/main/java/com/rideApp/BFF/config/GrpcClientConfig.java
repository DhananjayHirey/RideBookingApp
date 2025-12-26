package com.rideApp.BFF.config;

import com.rideApp.BFF.grpc.interceptor.GrpcJwtInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel userServiceChannel(GrpcJwtInterceptor jwtInterceptor) {
        return ManagedChannelBuilder
                .forAddress("user-service", 9090)
                .usePlaintext() // TLS later (prod)
                .intercept(jwtInterceptor)
                .build();
    }

    @Bean
    public ManagedChannel orderServiceChannel(GrpcJwtInterceptor jwtInterceptor) {
        return ManagedChannelBuilder
                .forAddress("order-service", 9091)
                .usePlaintext()
                .intercept(jwtInterceptor)
                .build();
    }

    @Bean
    public ManagedChannel notificationServiceChannel(GrpcJwtInterceptor jwtInterceptor) {
        return ManagedChannelBuilder
                .forAddress("notification-service", 9092)
                .usePlaintext()
                .intercept(jwtInterceptor)
                .build();
    }
}
