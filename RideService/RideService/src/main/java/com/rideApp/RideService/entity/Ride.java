package com.rideApp.RideService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID riderId;

    private double pickupLat;
    private double pickupLng;
    private double dropLat;
    private double dropLng;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    private Instant createdAt;
}
