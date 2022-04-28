package com.learn.reggie.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    public Object get(String key){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void set(String key, Object value){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,value);
    }

    public void remove(String key){
        redisTemplate.delete(key);
    }
}
