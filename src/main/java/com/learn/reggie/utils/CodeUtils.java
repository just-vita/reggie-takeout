package com.learn.reggie.utils;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = {"smsCode"})
public class CodeUtils {
    @CachePut(key = "#phone")
    public String generator(String phone) {
        return ValidateCodeUtils.generateValidateCode(6).toString();
    }

    @Cacheable(key = "#phone")
    @CacheEvict(key = "#phone")
    public String get(String phone) {
        return null;
    }

}
