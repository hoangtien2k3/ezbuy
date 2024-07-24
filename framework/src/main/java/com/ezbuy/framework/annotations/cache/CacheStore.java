package com.ezbuy.framework.annotations.cache;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.PostConstruct;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;

import com.ezbuy.framework.annotations.LocalCache;

import io.prometheus.client.cache.caffeine.CacheMetricsCollector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CacheStore {
    private static final CacheMetricsCollector CACHE_METRICS_COLLECTOR = new CacheMetricsCollector().register();
    private static final HashMap<String, Cache> caches = new HashMap<>();
    private static final Set<Method> autoLoadMethods = new HashSet<>();
    private static String reflectionPath = "com.ezbuy";

    @PostConstruct
    private static void init() {
        log.info("Start initializing cache");
        Reflections reflections = new Reflections(reflectionPath, Scanners.MethodsAnnotated);
        Set<Method> methods =
                reflections.get(Scanners.MethodsAnnotated.with(LocalCache.class).as(Method.class));
        for (Method method : methods) {
            String className = method.getDeclaringClass().getSimpleName();
            LocalCache localCache = method.getAnnotation(LocalCache.class);
            Integer maxRecord = localCache.maxRecord();
            Integer durationInMinute = localCache.durationInMinute();
            String cacheName = className + "." + method.getName();
            boolean autoLoad = localCache.autoCache();
            Cache<Object, Object> cache;
            if (autoLoad && (method.getParameterCount() == 0)) {
                cache = Caffeine.newBuilder()
                        .scheduler(Scheduler.systemScheduler())
                        .expireAfterWrite(Duration.ofMinutes(durationInMinute))
                        .recordStats()
                        .maximumSize(maxRecord)
                        .removalListener(new CustomizeRemovalListener(method))
                        .build();
                autoLoadMethods.add(method);
            } else {
                cache = Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(durationInMinute))
                        .recordStats()
                        .maximumSize(maxRecord)
                        .build();
            }
            caches.put(cacheName, cache);
            CACHE_METRICS_COLLECTOR.addCache(cacheName, cache);
        }
        log.info("Finish initializing {} cache", caches.size());
    }

    public static Cache getCache(String key) {
        return caches.get(key);
    }

    @Async
    @EventListener
    public void autoLoad(ContextRefreshedEvent event) {
        if (!autoLoadMethods.isEmpty()) {
            log.info("Start auto load {} cache", autoLoadMethods.size());
            for (Method method : autoLoadMethods) {
                CacheUtils.invokeMethod(method);
            }
            log.info("Finish auto load cache");
        }
    }
}
