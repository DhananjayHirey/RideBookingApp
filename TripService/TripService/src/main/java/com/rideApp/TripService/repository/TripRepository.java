package com.rideApp.TripService.repository;

import com.rideApp.TripService.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository
        extends JpaRepository<Trip, UUID> {}
