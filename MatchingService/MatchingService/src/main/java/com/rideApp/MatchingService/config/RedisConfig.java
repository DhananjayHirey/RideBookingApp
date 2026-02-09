package com.rideApp.MatchingService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(host,port));
    }

    @Bean
    @Primary
    public RedisTemplate<String,String> redisTemplate(
            RedisConnectionFactory factory){

        RedisTemplate<String,String> t = new RedisTemplate<>();
        t.setConnectionFactory(factory);

        StringRedisSerializer s = new StringRedisSerializer();
        t.setKeySerializer(s);
        t.setValueSerializer(s);

        t.afterPropertiesSet();
        return t;
    }
}
