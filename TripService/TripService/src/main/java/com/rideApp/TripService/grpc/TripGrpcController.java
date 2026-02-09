package com.rideApp.TripService.grpc;

import com.rideApp.TripService.StartTripRequest;
import com.rideApp.TripService.TripResponse;
import com.rideApp.TripService.TripServiceGrpc;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class TripGrpcController extends TripServiceGrpc.TripServiceImplBase {

    @Override
    public void startTrip(StartTripRequest request,
                          io.grpc.stub.StreamObserver<TripResponse> responseObserver) {

        TripResponse response = TripResponse.newBuilder()
                .setTripId(request.getTripId())
                .setStatus("STARTED")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}