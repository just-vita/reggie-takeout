package com.learn.reggie.utils;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CodeUtils {
    @CachePut(value = "smsCode", key = "#phone")
    public String generator(String phone) {
        return ValidateCodeUtils.generateValidateCode(6).toString();
    }

    @Cacheable(value = "smsCode", key = "#phone")
    public String get(String phone) {
        return null;
    }

}
