package com.rideApp.TripService.event;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DriverMatchedEvent{
    private UUID rideId;
    private String driverId;
}