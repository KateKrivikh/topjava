package ru.javawebinar.topjava.service;

import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {
    @Bean
    CacheManager cacheManager() {
        return new NoOpCacheManager();
    }
}
