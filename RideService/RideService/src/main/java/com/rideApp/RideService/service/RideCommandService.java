package com.rideApp.RideService.service;

import com.rideApp.RideService.CreateRideRequest;
import com.rideApp.RideService.entity.Ride;
import com.rideApp.RideService.entity.RideStatus;
import com.rideApp.RideService.event.RideRequestedEvent;
import com.rideApp.RideService.repository.RideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RideCommandService {

    private final RideRepository rideRepository;
    private final KafkaTemplate<String, RideRequestedEvent> kafkaTemplate;



    @Transactional
    public Ride createRide(CreateRideRequest request) {

        UUID rideId = UUID.randomUUID();

        Ride ride = Ride.builder()
                .id(rideId)
                .riderId(UUID.fromString(request.getRiderId()))
                .pickupLat(BigDecimal.valueOf(request.getPickupLat()))
                .pickupLng(BigDecimal.valueOf(request.getPickupLng()))
                .dropLat(BigDecimal.valueOf(request.getDropLat()))
                .dropLng(BigDecimal.valueOf(request.getDropLng()))
                .status(RideStatus.REQUESTED)
                .createdAt(Instant.now())
                .build();

        rideRepository.save(ride);

        RideRequestedEvent event =
                new RideRequestedEvent(
                        rideId,
                        ride.getRiderId(),
                        ride.getPickupLat().doubleValue(),
                        ride.getPickupLng().doubleValue()
                );

        kafkaTemplate.send("ride.requested", rideId.toString(), event);

        return ride;
    }


    @Transactional
    public void cancelRide(UUID rideId, UUID riderId) {

        Ride ride = rideRepository
                .findByIdAndRiderId(rideId, riderId)
                .orElseThrow(() -> new IllegalStateException("Ride not found"));

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new IllegalStateException("Ride cannot be cancelled");
        }

        ride.setStatus(RideStatus.CANCELLED);
    }
}
