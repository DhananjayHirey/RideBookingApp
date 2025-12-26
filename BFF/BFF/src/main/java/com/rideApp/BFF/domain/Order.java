package com.rideApp.BFF.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String orderId;
    private String status;
    private double amount;

    /**
     * Epoch time (milliseconds)
     * Keep it raw — formatting is DTO / frontend concern
     */
    private long createdAtEpoch;
}
