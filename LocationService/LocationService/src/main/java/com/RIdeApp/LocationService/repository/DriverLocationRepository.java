package com.RIdeApp.LocationService.repository;

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

    private static final String KEY = "drivers:available";

    private final RedisTemplate<String,String> redis;

    public void updateLocation(String driverId,double lat,double lng,boolean available){

        if(!available){
            redis.opsForGeo().remove(KEY,driverId);
            return;
        }

        redis.opsForGeo()
                .add(KEY,new Point(lng,lat),driverId);
    }

    public List<String> findNearby(double lat, double lng, int radius, int limit){

        GeoResults<RedisGeoCommands.GeoLocation<String>> res =
                redis.opsForGeo().radius(
                        KEY,
                        new Circle(new Point(lng,lat),
                                new Distance(radius, Metrics.METERS)),
                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                                .sortAscending()
                                .limit(limit));

        if(res==null) return List.of();

        return res.getContent().stream()
                .map(r->r.getContent().getName())
                .toList();
    }
}
