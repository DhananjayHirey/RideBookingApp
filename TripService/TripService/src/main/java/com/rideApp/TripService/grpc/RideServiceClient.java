package com.rideApp.TripService.grpc;


import com.rideApp.RideService.MarkRideMatchedRequest;
import com.rideApp.RideService.RideServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RideServiceClient {

    @GrpcClient("ride-service")
    private RideServiceGrpc.RideServiceBlockingStub stub;

    public void markRideMatched(UUID rideId, String driverId){

        stub.markRideMatched(
                MarkRideMatchedRequest.newBuilder()
                        .setRideId(rideId.toString())
                        .setDriverId(driverId)
                        .build()
        );
    }
}
