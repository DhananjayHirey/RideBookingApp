package com.rideApp.LocationService.controller;


import com.rideApp.LocationService.repository.DriverLocationRepository;
import com.rideApp.location.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class LocationGrpcController extends LocationServiceGrpc.LocationServiceImplBase {
    private final DriverLocationRepository repository;
    @Override
    public StreamObserver<LocationUpdate>streamDriverLocation(StreamObserver<Ack>responseObserver){
        return new StreamObserver<>(){
            @Override
            public void onNext(LocationUpdate update){
                repository.updateLocation(
                        update.getDriverId(),
                        update.getLat(),
                        update.getLng(),
                        update.getAvailable()
                );
            }
            @Override
            public void onError(Throwable t) {
                // log and close stream
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(
                        Ack.newBuilder().setSuccess(true).build()
                );
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void findNearbyDrivers(
            NearbyDriversRequest request,
            StreamObserver<NearbyDriversResponse> responseObserver) {

        List<String> drivers =
                repository.findNearby(
                        request.getLat(),
                        request.getLng(),
                        request.getRadiusMeters(),
                        request.getLimit()
                );

        responseObserver.onNext(
                NearbyDriversResponse.newBuilder()
                        .addAllDriverIds(drivers)
                        .build()
        );
        responseObserver.onCompleted();
    }


}
