package com.rideApp.BFF.controller;

import com.rideApp.BFF.dto.CreateRideHttpRequest;
import com.rideApp.BFF.grpc.client.RideGrpcClient;
import com.rideApp.RideService.CreateRideResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rides")
public class RideController {

    private final RideGrpcClient rideGrpcClient;

    @PostMapping
    public Mono<ResponseEntity<CreateRideResponse>> createRide(
            @RequestBody Mono<CreateRideHttpRequest> requestMono,
            @RequestHeader("X-USER-ID") String userId
    ) {
        return requestMono
                .flatMap(req ->
                        rideGrpcClient.createRide(
                                userId,
                                req.getPickupLat(),
                                req.getPickupLng(),
                                req.getDropLat(),
                                req.getDropLng()
                        )
                )
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{rideId}")
    public Mono<ResponseEntity<Void>> cancelRide(
            @PathVariable String rideId,
            @RequestHeader("X-USER-ID") String userId
    ) {
        return rideGrpcClient
                .cancelRide(rideId, userId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
