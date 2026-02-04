package com.rideApp.TripService.event;
import java.util.UUID;

public record DriverMatchedEvent(
        UUID rideId,
        String driverId
) {}