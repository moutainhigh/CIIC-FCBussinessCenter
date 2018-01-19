package com.ciicsh.caldispatchjob.compute.util;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Created by bill on 17/9/12.
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        GuavaCacheManager cacheManager = new GuavaCacheManager();

        cacheManager.setCacheBuilder(CacheBuilder
                .newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .maximumSize(1000)
        );
        cacheManager.setCacheNames(Arrays.asList("payItems"));
//        Cache cache = cacheManager.getCache("payItems");
//        cache.put("payItemId", new PayItem()); // todo call dao get payItem list

        return cacheManager;
    }
}
