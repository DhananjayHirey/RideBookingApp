package com.rideApp.RideService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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


    @Column(name = "drop_lat", precision = 9, scale = 6)
    private BigDecimal dropLat;

    @Column(name = "drop_lng", precision = 9, scale = 6)
    private BigDecimal dropLng;

    @Column(name = "pickup_lat", precision = 9, scale = 6)
    private BigDecimal pickupLat;

    @Column(name = "pickup_lng", precision = 9, scale = 6)
    private BigDecimal pickupLng;
    @Enumerated(EnumType.STRING)
    private RideStatus status;

    private Instant createdAt;
}
