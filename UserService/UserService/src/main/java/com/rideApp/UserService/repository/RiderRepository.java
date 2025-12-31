package com.rideApp.UserService.repository;

import com.rideApp.UserService.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RiderRepository extends JpaRepository<Rider, UUID> {
    Optional<Rider> findByUserId(UUID userId);
}


