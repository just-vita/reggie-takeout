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
		cacheNames.add("employee_getById");
		cacheNames.add("category_page");
		cacheNames.add("category_getById");
		cacheNames.add("category_list");
		cacheNames.add("dish_page");
		cacheNames.add("dish_getById");
		cacheNames.add("dish_list");
		cacheNames.add("order_page");
		cacheNames.add("order_userPage");
		cacheNames.add("setmeal_page");
		cacheNames.add("setmeal_getById");
		cacheNames.add("setmeal_list");
		cacheNames.add("address_page");
		cacheNames.add("address_default");
		cacheNames.add("address_getById");
		cacheNames.add("loginUser");
		cacheNames.add("smsCode");

		ConcurrentHashMap configMap = new ConcurrentHashMap<>();
        //自定义缓存时间
		RedisCacheConfiguration expiration = config.entryTtl(Duration.ofMinutes(30L));
		configMap.put("employee_page", expiration);
		configMap.put("employee_getById", expiration);
		configMap.put("category_page", expiration);
		configMap.put("category_getById", expiration);
		configMap.put("category_list", expiration);
		configMap.put("dish_page", expiration);
		configMap.put("dish_list", expiration);
		configMap.put("order_page", expiration);
		configMap.put("order_userPage", expiration);
		configMap.put("setmeal_page", expiration);
		configMap.put("setmeal_getById", expiration);
		configMap.put("setmeal_list", expiration);
		configMap.put("address_page", expiration);
		configMap.put("address_default", expiration);
		configMap.put("address_getById", expiration);
		configMap.put("loginUser", config.entryTtl(Duration.ofDays(1L)));
		configMap.put("smsCode", config.entryTtl(Duration.ofMinutes(10L)));

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
