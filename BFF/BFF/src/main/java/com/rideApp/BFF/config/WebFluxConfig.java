package com.rideApp.BFF.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",   // local frontend
                        "https://your-frontend.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public WebFilter reactorContextWebFilter() {
        return (exchange, chain) -> {

            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(org.springframework.http.HttpHeaders.AUTHORIZATION);

            return chain.filter(exchange)
                    .contextWrite(ctx -> {
                                if (authHeader != null) {
                                    return ctx
                                            .put(ServerWebExchange.class, exchange)
                                            .put("AUTH_HEADER", authHeader);
                                }
                                return ctx.put(ServerWebExchange.class, exchange);
                            }
                    );
        };
    }

}
