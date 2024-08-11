//package com.ezbuy.framework.annotations.cache;
//
//import io.prometheus.client.cache.caffeine.CacheMetricsCollector;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class CacheMetricsConfig {
//    private static volatile CacheMetricsCollector cacheMetricsCollector;
//
//    @Bean
//    public CacheMetricsCollector cacheMetricsCollector() {
//        if (cacheMetricsCollector == null) {
//            synchronized (CacheMetricsConfig.class) {
//                if (cacheMetricsCollector == null) {
//                    cacheMetricsCollector = new CacheMetricsCollector().register();
//                }
//            }
//        }
//        return cacheMetricsCollector;
//    }
//}
