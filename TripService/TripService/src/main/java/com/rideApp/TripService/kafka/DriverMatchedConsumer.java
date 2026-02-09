package com.rideApp.TripService.kafka;

import com.rideApp.TripService.event.DriverMatchedEvent;
import com.rideApp.TripService.service.TripCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverMatchedConsumer {

    private final TripCommandService tripService;

    @KafkaListener(topics="driver.matched",groupId="trip")
    public void consume(DriverMatchedEvent e){

        System.out.println("Driver Matched Event consumed in TripService: ");
        System.out.println("Ride ID: "+e.getRideId());
        System.out.println("Driver ID: "+e.getDriverId());

        tripService.startTrip(e.getRideId(), e.getDriverId());
    }
}
