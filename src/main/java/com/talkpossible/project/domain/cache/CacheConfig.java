package com.talkpossible.project.domain.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    //Map형식으로 사용하기 위해 ConcurrentMapCacheManager 사용함
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("MyCache");
    }
}
