package com.rideApp.MatchingService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class RideRequested {
    private UUID rideId;
    private UUID riderId;
    private double pickupLat;
    private double pickupLng;
}