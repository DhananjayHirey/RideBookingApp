package com.rideApp.UserService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    @Id
    private UUID id = UUID.randomUUID();

    private UUID driverId;
    private String plateNumber;
    private String type;

    private Instant createdAt = Instant.now();
}
