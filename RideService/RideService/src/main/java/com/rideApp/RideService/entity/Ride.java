package com.rideApp.RideService.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    // 🔴 FIX #1 — status must be NOT NULL + default value
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideStatus status = RideStatus.REQUESTED;

    // 🔴 FIX #2 — auto-populate created_at + not nullable
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
