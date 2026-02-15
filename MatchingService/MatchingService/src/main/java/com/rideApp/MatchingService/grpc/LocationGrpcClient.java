package com.rideApp.MatchingService.grpc;



import com.rideApp.location.ClaimDriverRequest;
import com.rideApp.location.LocationServiceGrpc;
import com.rideApp.location.NearbyDriversRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationGrpcClient {

    @GrpcClient("location-service")
    private LocationServiceGrpc.LocationServiceBlockingStub stub;

    public List<String> findNearby(double lat, double lng) {
        NearbyDriversRequest req = NearbyDriversRequest.newBuilder()
                .setLat(lat)
                .setLng(lng)
                .setRadiusMeters(3000)
                .setLimit(10)
                .build();

        return stub.findNearbyDrivers(req).getDriverIdsList();
    }

    public boolean claimDriver(String driverId, String rideId) {
        ClaimDriverRequest req = ClaimDriverRequest.newBuilder()
                .setDriverId(driverId)
                .setRideId(rideId)
                .build();

        return stub.claimDriver(req).getSuccess();
    }
}