package com.project.homeFinder.config;

import com.project.homeFinder.dto.cache.SerializeListOfApartmentToSubway;
import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties; //prefix="spring.data.redis"=>application.yml 에서 해당 prefix 를 가진 값들을 불러와서 RedisProperties 에 있는 파라미터들로 주입(url, host, username 등)

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisURI redisUrI = RedisURI.create(redisProperties.getUrl());
        RedisConfiguration configuration = LettuceConnectionFactory.createRedisConfiguration(redisUrI);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
        factory.afterPropertiesSet();   // afterPropertiesSet() 를 해줘야 factory 가 initialized = true 로 변경됨

        return factory;
    }

    /**
     * Redis의 Value 값은 Serialize 가능한 객체여야 함
     * -> List를 비롯한 Collection은 Serialize 불가능
     *  -> List를 파라미터로 갖는 객체 생성(SerializeListOfApartmentToSubway)
     */
    @Bean
    public RedisTemplate<String, SerializeListOfApartmentToSubway> apartmentToSubwayRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, SerializeListOfApartmentToSubway> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<SerializeListOfApartmentToSubway>(SerializeListOfApartmentToSubway.class));

        return redisTemplate;
    }

}
