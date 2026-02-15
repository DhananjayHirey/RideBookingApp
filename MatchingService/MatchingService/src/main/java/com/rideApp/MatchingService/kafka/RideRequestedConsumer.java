package com.rideApp.MatchingService.kafka;

import com.rideApp.MatchingService.grpc.LocationGrpcClient;
import com.rideApp.MatchingService.model.DriverMatched;
import com.rideApp.MatchingService.model.RideRequested;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideRequestedConsumer {

    private final LocationGrpcClient locationClient;
    private final KafkaTemplate<String, DriverMatched> kafka;

    @KafkaListener(topics = "ride.requested", groupId = "matching")
    public void consume(RideRequested r) {
        System.out.println("🔥 Received ride: ");
        System.out.println("RideID: "+r.getRideId());
        System.out.println("RiderID: "+r.getRiderId());
        System.out.println("PickupLat: "+r.getPickupLat());
        System.out.println("PickupLng: "+r.getPickupLng());
        var drivers = locationClient.findNearby(
                r.getPickupLat(),
                r.getPickupLng());
        System.out.println("Nearby drivers found: " + drivers.size());
        System.out.println("Drivers: " + drivers);
        for (String d : drivers) {

            boolean claimed = locationClient.claimDriver(
                    d,
                    r.getRideId().toString());


            if (claimed) {
                System.out.println("Driver confirmed: "+d);
                System.out.println("Driver Matched event sent");
                kafka.send("driver.matched",
                        new DriverMatched(r.getRideId(), d));
                return;
            }
        }
    }
}
