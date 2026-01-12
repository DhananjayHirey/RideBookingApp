package com.rideApp.RideService.controller;

import com.rideApp.RideService.*;
import com.rideApp.RideService.CreateRideRequest;
import com.rideApp.RideService.CreateRideResponse;
import com.rideApp.RideService.RideEmpty;
import com.rideApp.RideService.RideServiceGrpc;
import com.rideApp.RideService.entity.Ride;
import com.rideApp.RideService.service.RideCommandService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.util.UUID;

//import static sun.java2d.marlin.CollinearSimplifier.SimplifierState.RideEmpty;

@GrpcService
@RequiredArgsConstructor
public class RideGrpcController extends RideServiceGrpc.RideServiceImplBase {

    private final RideCommandService rideCommandService;

    @Override
    public void createRide(
            CreateRideRequest request,
            StreamObserver<CreateRideResponse> responseObserver) {

        Ride ride = rideCommandService.createRide(request);

        responseObserver.onNext(
                CreateRideResponse.newBuilder()
                        .setRideId(ride.getId().toString())
                        .setStatus(ride.getStatus().name())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void cancelRide(
            com.rideApp.RideService.CancelRideRequest request,
            StreamObserver<RideEmpty> responseObserver) {

        rideCommandService.cancelRide(
                UUID.fromString(request.getRideId()),
                UUID.fromString(request.getRiderId())
        );

        responseObserver.onNext(RideEmpty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
