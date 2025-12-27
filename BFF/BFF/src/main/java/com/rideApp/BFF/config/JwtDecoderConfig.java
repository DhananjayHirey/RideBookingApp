package com.rideApp.BFF.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtDecoderConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {

        SecretKey secretKey = new SecretKeySpec(
                jwtSecret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
    }
}