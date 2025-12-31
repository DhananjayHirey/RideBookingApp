package com.rideApp.UserService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
public class Driver {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private DriverStatus status = DriverStatus.OFFLINE;

    private BigDecimal rating = BigDecimal.valueOf(5.0);
    private Instant createdAt = Instant.now();
}
