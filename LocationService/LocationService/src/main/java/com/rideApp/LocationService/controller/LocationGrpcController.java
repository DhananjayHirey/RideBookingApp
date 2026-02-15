package com.rideApp.LocationService.controller;

import com.rideApp.LocationService.repository.DriverLocationRepository;
//import com.rideApp.location.*;
import com.rideApp.location.Ack;
import com.rideApp.location.ClaimDriverRequest;
import com.rideApp.location.ClaimDriverResponse;
import com.rideApp.location.LocationServiceGrpc;
import com.rideApp.location.LocationUpdate;
import com.rideApp.location.NearbyDriversRequest;
import com.rideApp.location.NearbyDriversResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
//@RequiredArgsConstructor
public class LocationGrpcController
        extends LocationServiceGrpc.LocationServiceImplBase {

    private final DriverLocationRepository repo;

    public LocationGrpcController(DriverLocationRepository repo) {
        this.repo = repo;
    }

    @Override
    public StreamObserver<LocationUpdate> streamDriverLocation(
            StreamObserver<Ack> response){

        return new StreamObserver<>(){

            public void onNext(LocationUpdate u){
                repo.updateLocation(
                        u.getDriverId(),
                        u.getLat(),
                        u.getLng(),
                        u.getAvailable());
            }

            public void onError(Throwable t){}

            public void onCompleted(){
                response.onNext(Ack.newBuilder().setSuccess(true).build());
                response.onCompleted();
            }
        };
    }

    @Override
    public void findNearbyDrivers(
            NearbyDriversRequest r,
            StreamObserver<NearbyDriversResponse> o){

        var drivers=repo.findNearby(
                r.getLat(),r.getLng(),
                r.getRadiusMeters(),
                r.getLimit());

        o.onNext(NearbyDriversResponse.newBuilder()
                .addAllDriverIds(drivers).build());

        o.onCompleted();
    }

    @Override
    public void claimDriver(
            ClaimDriverRequest req,
            StreamObserver<ClaimDriverResponse> responseObserver) {

        boolean success = repo.claimDriver(
                req.getDriverId(),
                req.getRideId());

        responseObserver.onNext(
                ClaimDriverResponse.newBuilder()
                        .setSuccess(success)
                        .build()
        );

        responseObserver.onCompleted();
    }

}
