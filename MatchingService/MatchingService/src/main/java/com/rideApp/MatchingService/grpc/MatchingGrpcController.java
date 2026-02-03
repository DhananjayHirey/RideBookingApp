package com.rideApp.MatchingService.grpc;

import com.rideApp.MatchingService.repository.DriverGeoRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;



@GrpcService
public class MatchingGrpcController
        extends com.rideApp.matching.MatchingServiceGrpc.MatchingServiceImplBase {

    private final DriverGeoRepository repo;

    public MatchingGrpcController(DriverGeoRepository repo){
        this.repo = repo;
    }

    @Override
    public void matchRide(
            com.rideApp.matching.MatchRideRequest req,
            StreamObserver<com.rideApp.matching.MatchRideResponse> o){

        var drivers = repo.nearby(req.getLat(),req.getLng());

        for(String d:drivers){

            if(repo.lockDriver(d,req.getRideId())){

                repo.removeFromPool(d);

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
