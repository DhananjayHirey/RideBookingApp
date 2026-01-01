package com.rideApp.BFF.controller;

import com.rideApp.BFF.dto.CreateDriverHttpRequest;
import com.rideApp.BFF.dto.CreateRiderHttpRequest;
import com.rideApp.BFF.grpc.client.UserGrpcClient;
import com.rideApp.UserService.DriverProfile;
import com.rideApp.UserService.RiderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserGrpcClient userGrpcClient;

    // -------- CREATE --------

    @PostMapping("/rider")
    public Mono<Void> createRider(@RequestBody CreateRiderHttpRequest request) {
        return userGrpcClient.createRider(request.getName());
    }

    @PostMapping("/driver")
    public Mono<Void> createDriver(@RequestBody CreateDriverHttpRequest request) {
        return userGrpcClient.createDriver();
    }

    // -------- GET --------

    @GetMapping("/rider/profile")
    public Mono<RiderProfile> getRiderProfile() {
        return userGrpcClient.getRiderProfile();
    }

    @GetMapping("/driver/profile")
    public Mono<DriverProfile> getDriverProfile() {
        return userGrpcClient.getDriverProfile();
    }
}
