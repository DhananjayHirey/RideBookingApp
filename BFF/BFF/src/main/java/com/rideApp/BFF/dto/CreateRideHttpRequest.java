package com.rideApp.BFF.dto;

import lombok.Data;

@Data
public class CreateRideHttpRequest {
    private double pickupLat;
    private double pickupLng;
    private double dropLat;
    private double dropLng;
}