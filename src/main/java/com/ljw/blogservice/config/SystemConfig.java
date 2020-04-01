package com.ljw.blogservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class SystemConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public RedisTemplate redisTemplateInit() {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        //string、hash、list、set、zset
        //key-value
        //设置序列化器
        this.redisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
        this.redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        this.redisTemplate.setHashKeySerializer(stringSerializer);
        this.redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }

}
