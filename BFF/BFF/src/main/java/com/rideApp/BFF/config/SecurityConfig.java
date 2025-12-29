package com.rideApp.BFF.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers("/actuator/**","/auth/login","/auth/register","/auth/refresh").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                .build();
    }

    @Bean
    public Converter<Jwt, Mono<JwtAuthenticationToken>> jwtAuthenticationConverter() {
        return jwt -> {

            String userId = jwt.getSubject();
            List<String> roles = jwt.getClaimAsStringList("roles");

            Collection<? extends GrantedAuthority> authorities =
                    roles == null
                            ? List.of()
                            : roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .toList();

            return Mono.just(new JwtAuthenticationToken(jwt, authorities, userId));
        };
    }

}
