package com.rideApp.MatchingService.kafka;

import com.rideApp.MatchingService.model.DriverMatched;
import com.rideApp.MatchingService.model.RideRequested;
import com.rideApp.MatchingService.repository.DriverGeoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideRequestedConsumer {

    private final DriverGeoRepository repo;
    private final KafkaTemplate<String, DriverMatched> kafka;

    @KafkaListener(topics="ride.requested",groupId="matching")
    public void consume(RideRequested r){

        var drivers = repo.nearby(r.lat(),r.lng());

        for(String d:drivers){

            if(repo.lockDriver(d,r.rideId())){

                repo.removeFromPool(d);

                kafka.send("driver.matched",
                        new DriverMatched(r.rideId(),d));

                return;
            }
        }
    }
}
