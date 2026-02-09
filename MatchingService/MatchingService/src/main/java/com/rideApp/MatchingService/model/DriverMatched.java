package com.rideApp.MatchingService.model;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DriverMatched {
    private UUID rideId;
    private String driverId;
}