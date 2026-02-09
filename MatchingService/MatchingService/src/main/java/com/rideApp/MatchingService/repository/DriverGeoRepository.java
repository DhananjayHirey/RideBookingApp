package com.rideApp.MatchingService.repository;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
public class DriverGeoRepository {

    private static final String GEO = "drivers:available";
    private static final String LOCK = "driver:lock:";

    private final RedisTemplate<String, String> redis;

    public DriverGeoRepository(RedisTemplate<String, String> redis) {
        this.redis = redis;
    }

    public List<String> nearby(double lat, double lng) {

        GeoResults<RedisGeoCommands.GeoLocation<String>> res = redis.opsForGeo().radius(
                GEO,
                new Circle(new Point(lng, lat),
                        new Distance(3000, Metrics.METERS)));

        if (res == null)
            return List.of();

        return res.getContent().stream()
                .map(r -> r.getContent().getName())
                .toList();
    }

    public boolean lockDriver(String driverId, java.util.UUID rideId) {

        Boolean ok = redis.opsForValue().setIfAbsent(
                LOCK + driverId,
                rideId.toString(),
                Duration.ofSeconds(30));

        return Boolean.TRUE.equals(ok);
    }

    public void removeFromPool(String driverId) {
        redis.opsForGeo().remove(GEO, driverId);
    }
}
