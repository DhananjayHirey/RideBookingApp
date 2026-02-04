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
    public void onDriverMatched(DriverMatchedEvent e){

        tripService.startTrip(e.rideId(), e.driverId());
    }
}
