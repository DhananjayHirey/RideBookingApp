package com.rideApp.LocationService.model;


import lombok.Data;

@Data
public class LocationUpdatePayload {
    private String driverId;
    private double lat;
    private double lng;
    private boolean available;
}
//@KafkaListener(topics = "driver.matched")
//public void onDriverMatched(DriverMatched event) {
//
//    socketHandler.sendMessageToDriver(
//            event.getDriverId(),
//            "{\"type\":\"RIDE_ASSIGNED\",\"rideId\":\"" + event.getRideId() + "\"}"
//    );
//}
