package com.rideApp.UserService.grpc;

import com.rideApp.UserService.*;
import com.rideApp.UserService.CreateDriverRequest;
import com.rideApp.UserService.CreateRiderRequest;
import com.rideApp.UserService.DriverProfile;
import com.rideApp.UserService.Empty;
import com.rideApp.UserService.GetProfileRequest;
import com.rideApp.UserService.RiderProfile;
import com.rideApp.UserService.UserServiceGrpc;
import com.rideApp.UserService.entity.Driver;
import com.rideApp.UserService.entity.DriverStatus;
import com.rideApp.UserService.entity.Rider;
import com.rideApp.UserService.repository.DriverRepository;
import com.rideApp.UserService.repository.RiderRepository;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

import java.util.UUID;


@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase{
    private final RiderRepository riderRepo;
    private final DriverRepository driverRepo;

    public UserGrpcService(RiderRepository riderRepo,
                           DriverRepository driverRepo) {
        this.riderRepo = riderRepo;
        this.driverRepo = driverRepo;
    }

    @Override
    public void createRider(CreateRiderRequest request,
                            StreamObserver<Empty> observer) {

        Rider rider = new Rider();
        rider.setUserId(UUID.fromString(request.getUserId()));
        rider.setName(request.getName());

        riderRepo.save(rider);

        observer.onNext(Empty.newBuilder().build());
        observer.onCompleted();
    }

    @Override
    public void getRiderProfile(GetProfileRequest request,
                                StreamObserver<com.rideApp.UserService.RiderProfile> observer) {

        Rider rider = riderRepo.findByUserId(UUID.fromString(request.getUserId()))
                .orElseThrow();

        observer.onNext(
                RiderProfile.newBuilder()
                        .setUserId(rider.getUserId().toString())
                        .setName(rider.getName())
                        .setRating(rider.getRating().doubleValue())
                        .setTotalRides(rider.getTotalRides())
                        .build()
        );

        observer.onCompleted();
    }

    @Override
    public void createDriver(CreateDriverRequest request,
                             StreamObserver<Empty> observer) {

        UUID userId = UUID.fromString(request.getUserId());

        if (driverRepo.findByUserId(userId).isPresent()) {
            observer.onError(
                    io.grpc.Status.ALREADY_EXISTS
                            .withDescription("Driver profile already exists")
                            .asRuntimeException()
            );
            return;
        }

        Driver driver = new Driver();
        driver.setUserId(userId);
        driver.setStatus(DriverStatus.OFFLINE);

        driverRepo.save(driver);

        observer.onNext(Empty.newBuilder().build());
        observer.onCompleted();
    }

    @Override
    public void getDriverProfile(GetProfileRequest request,
                                 StreamObserver<DriverProfile> observer) {

        Driver driver = driverRepo
                .findByUserId(UUID.fromString(request.getUserId()))
                .orElseThrow(() ->
                        io.grpc.Status.NOT_FOUND
                                .withDescription("Driver profile not found")
                                .asRuntimeException()
                );

        observer.onNext(
                DriverProfile.newBuilder()
                        .setUserId(driver.getUserId().toString())
                        .setStatus(driver.getStatus().name())
                        .setRating(driver.getRating().doubleValue())
                        .build()
        );

        observer.onCompleted();
    }


}
