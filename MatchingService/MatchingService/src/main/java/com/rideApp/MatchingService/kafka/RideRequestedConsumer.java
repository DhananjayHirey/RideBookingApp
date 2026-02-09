package com.rideApp.MatchingService.kafka;

import com.rideApp.MatchingService.model.DriverMatched;
import com.rideApp.MatchingService.model.RideRequested;
import com.rideApp.MatchingService.repository.DriverGeoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideRequestedConsumer {

    private final DriverGeoRepository repo;

    @Autowired
    private final KafkaTemplate<String, DriverMatched> kafka;

    @KafkaListener(topics = "ride.requested", groupId = "matching")
    public void consume(RideRequested r) {

        System.out.println("MatchingService received the requested ride: ");
        System.out.println("RideId: "+r.getRideId());
        System.out.println("Pickup Lat: "+r.getPickupLat());
        System.out.println("Pickup Lng: "+r.getPickupLng());
        System.out.println("RiderId: "+r.getRiderId());

        var drivers = repo.nearby(r.getPickupLat(), r.getPickupLng());

        for (String d : drivers) {

            if (repo.lockDriver(d, r.getRideId())) {

                repo.removeFromPool(d);
                System.out.println("Matched driver " + d + " for ride " + r.getRideId());

                kafka.send("driver.matched",
                        new DriverMatched(r.getRideId(), d));

                return;
            }
        }
    }
}
