package com.imarket.marketapi.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisInfo {
    @Value("${spring.redis.enable:false}")
    public boolean redisEnable;
}
