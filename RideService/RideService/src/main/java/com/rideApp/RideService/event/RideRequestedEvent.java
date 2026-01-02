package com.rideApp.RideService.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestedEvent {

    private UUID rideId;
    private UUID riderId;
    private double pickupLat;
    private double pickupLng;
}
