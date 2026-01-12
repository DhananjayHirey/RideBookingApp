package com.rideApp.BFF.grpc.client;

import com.rideApp.RideService.CancelRideRequest;
import com.rideApp.RideService.CreateRideRequest;
import com.rideApp.RideService.CreateRideResponse;
import com.rideApp.RideService.RideServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class RideGrpcClient {

//    @GrpcClient("ride-service")
    private RideServiceGrpc.RideServiceBlockingStub rideStub;

    public Mono<CreateRideResponse> createRide(
            String riderId,
            double pickupLat,
            double pickupLng,
            double dropLat,
            double dropLng
    ) {
        return Mono.fromCallable(() -> {
                    CreateRideRequest request =
                            CreateRideRequest.newBuilder()
                                    .setRiderId(riderId)
                                    .setPickupLat(pickupLat)
                                    .setPickupLng(pickupLng)
                                    .setDropLat(dropLat)
                                    .setDropLng(dropLng)
                                    .build();

                    return rideStub.createRide(request);
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> cancelRide(String rideId, String riderId) {
        return Mono.fromCallable(() -> {
                    CancelRideRequest request =
                            CancelRideRequest.newBuilder()
                                    .setRideId(rideId)
                                    .setRiderId(riderId)
                                    .build();

                    rideStub.cancelRide(request);
                    return true;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .then();
    }
}
