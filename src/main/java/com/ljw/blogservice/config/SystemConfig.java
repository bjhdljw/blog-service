package com.ljw.blogservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class SystemConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public RedisTemplate redisTemplateInit() {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        //string、hash、list、set、zset
        //key-value
        //设置序列化器
        this.redisTemplate.setKeySerializer(stringSerializer);
        this.redisTemplate.setValueSerializer(stringSerializer);
        this.redisTemplate.setHashKeySerializer(stringSerializer);
        this.redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }

}
