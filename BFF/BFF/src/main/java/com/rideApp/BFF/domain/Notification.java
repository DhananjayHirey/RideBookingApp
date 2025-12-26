package com.rideApp.BFF.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private String id;
    private String message;
    private boolean read;

    /**
     * Epoch time (milliseconds)
     */
    private long createdAtEpoch;
}
