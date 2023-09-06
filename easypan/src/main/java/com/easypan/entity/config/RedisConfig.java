package com.easypan.entity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisConfig<V> {

    @Bean
    public RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // set key serializer
        template.setKeySerializer(RedisSerializer.string());
        // set value serializer
        template.setValueSerializer(RedisSerializer.json());
        // set key serializer of hash
        template.setHashKeySerializer(RedisSerializer.string());
        // set value serializer of hash
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();
        return template;
    }
}
