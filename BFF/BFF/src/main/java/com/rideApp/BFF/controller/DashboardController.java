package com.rideApp.BFF.controller;

import com.rideApp.BFF.application.DashboardService;
import com.rideApp.BFF.dto.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public Mono<DashboardResponse> getDashboard(
            @AuthenticationPrincipal JwtAuthenticationToken auth
    ) {
        String userId = auth.getName(); // subject from JWT
        return dashboardService.getDashboard(userId);
    }
}