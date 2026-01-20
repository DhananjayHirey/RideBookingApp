package com.rideApp.LocationService.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class DriverLocationRepository {

    private static final String AVAILABLE_KEY = "drivers:available";

    private final RedisTemplate<String, String> redisTemplate;

    public void updateLocation(
            String driverId,
            double lat,
            double lng,
            boolean available) {

        if (!available) {
            redisTemplate.opsForGeo()
                    .remove(AVAILABLE_KEY, driverId);
            return;
        }

        redisTemplate.opsForGeo()
                .add(
                        AVAILABLE_KEY,
                        new Point(lng, lat),
                        driverId
                );
                System.out.println("Updated location for driver " + driverId);
                System.out.println("lat " + lat);
                System.out.println("lng " + lng);
    }

    public List<String> findNearby(
            double lat,
            double lng,
            int radiusMeters,
            int limit) {

        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo()
                        .radius(
                                AVAILABLE_KEY,
                                new Circle(
                                        new Point(lng, lat),
                                        new Distance(radiusMeters, Metrics.METERS)
                                ),
                                RedisGeoCommands.GeoRadiusCommandArgs
                                        .newGeoRadiusArgs()
                                        .sortAscending()
                                        .limit(limit)
                        );

        if (results == null) return List.of();

        return results.getContent()
                .stream()
                .map(r -> r.getContent().getName())
                .toList();
    }
}
