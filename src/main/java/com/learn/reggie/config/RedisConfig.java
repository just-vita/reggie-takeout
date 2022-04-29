package com.learn.reggie.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Configuration
@EnableCaching //启用缓存
public class RedisConfig {
 
	/**
	 * 自定义缓存管理器
	 */
	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

		Set cacheNames = new HashSet<>();
		cacheNames.add("employee_page");
		cacheNames.add("smsCode");

		ConcurrentHashMap configMap = new ConcurrentHashMap<>();
        //自定义缓存时间
		RedisCacheConfiguration expiration = config.entryTtl(Duration.ofMinutes(30L));
		configMap.put("employee_page", expiration);
		configMap.put("employeeById", expiration);
		configMap.put("smsCode", config.entryTtl(Duration.ofMinutes(5L)));

        //永久
//		configMap.put("", config);

		//需要先初始化缓存名称，再初始化其它的配置。
		RedisCacheManager cacheManager = RedisCacheManager.
				builder(factory).initialCacheNames(cacheNames).
				withInitialCacheConfigurations(configMap)
				.build();
		return cacheManager;
	}
} 
