package com.rideApp.TripService.service;

import com.rideApp.TripService.grpc.RideServiceClient;
import com.rideApp.TripService.model.Trip;
import com.rideApp.TripService.model.TripStatus;
import com.rideApp.TripService.repository.TripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripCommandService {

    private final TripRepository repo;
    private final RideServiceClient rideClient;

    @Transactional
    public void startTrip(UUID rideId, String driverId){

        Trip trip = Trip.builder()
                .tripId(UUID.randomUUID())
                .rideId(rideId)
                .driverId(driverId)
                .status(TripStatus.MATCHED)
                .createdAt(Instant.now())
                .build();

        repo.save(trip);

//        rideClient.markRideMatched(rideId, driverId);
    }
}
