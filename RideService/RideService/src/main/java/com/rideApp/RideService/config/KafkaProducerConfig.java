package com.rideApp.RideService.config;

import com.rideApp.RideService.event.RideRequestedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, RideRequestedEvent> kafkaTemplate(
            ProducerFactory<String, RideRequestedEvent> factory) {
        return new KafkaTemplate<>(factory);
    }
}

