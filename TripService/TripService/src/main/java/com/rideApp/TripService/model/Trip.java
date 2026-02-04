package com.rideApp.TripService.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="trips")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    private UUID tripId;

    private UUID rideId;
    private String driverId;

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    private Instant createdAt;
}
