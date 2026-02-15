package com.rideApp.MatchingService.grpc;

//NOTE : The current architecture triggers matching service in an event driven async manner...so these functions are just created to explicitly math a given specific ride in future if need synchronously.

import com.rideApp.MatchingService.grpc.LocationGrpcClient;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class MatchingGrpcController
        extends com.rideApp.matching.MatchingServiceGrpc.MatchingServiceImplBase {

    private final LocationGrpcClient locationClient;

    public MatchingGrpcController(LocationGrpcClient locationClient) {
        this.locationClient = locationClient;
    }

    @Override
    public void matchRide(
            com.rideApp.matching.MatchRideRequest req,
            StreamObserver<com.rideApp.matching.MatchRideResponse> o) {

        var drivers = locationClient.findNearby(
                req.getLat(),
                req.getLng()
        );

        for (String d : drivers) {

            boolean claimed = locationClient.claimDriver(
                    d,
                    req.getRideId()
            );

            if (claimed) {

                o.onNext(com.rideApp.matching.MatchRideResponse.newBuilder()
                        .setSuccess(true)
                        .setDriverId(d)
                        .build());

                o.onCompleted();
                return;
            }
        }

        o.onNext(com.rideApp.matching.MatchRideResponse.newBuilder()
                .setSuccess(false)
                .build());

        o.onCompleted();
    }
}
