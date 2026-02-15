package com.rideApp.LocationService.kafka;

import com.rideApp.LocationService.model.DriverMatched;
import com.rideApp.LocationService.socket.DriverSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DriverMatchedConsumer {

    private final DriverSocketHandler socketHandler;

    @KafkaListener(topics = "driver.matched", groupId="locationServiceConsumer")
    public void consume(DriverMatched event) throws IOException {
        System.out.println("LocationService received driver.matched event: ");
        System.out.println("RideId: "+event.getRideId());
        socketHandler.sendMessageToDriver(
                event.getDriverId(),
                "{\"type\":\"RIDE_ASSIGNED\",\"rideId\":\"" + event.getRideId() + "\"}"
        );
    }
}
