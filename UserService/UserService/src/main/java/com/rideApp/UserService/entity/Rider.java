package com.rideApp.UserService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "riders")
@Getter
@Setter
@NoArgsConstructor
public class Rider {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false, unique = true)
    private UUID userId;

    private String name;

    private BigDecimal rating = BigDecimal.valueOf(5.0);
    private int totalRides = 0;

    private Instant createdAt = Instant.now();
}
