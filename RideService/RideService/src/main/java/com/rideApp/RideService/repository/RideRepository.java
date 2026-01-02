package com.rideApp.RideService.repository;

import com.rideApp.RideService.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    Optional<Ride> findByIdAndRiderId(UUID id, UUID riderId);
}
