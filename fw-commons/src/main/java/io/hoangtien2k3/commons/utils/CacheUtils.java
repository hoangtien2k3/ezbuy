package io.hoangtien2k3.commons.utils;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.index.qual.NonNegative;
import reactor.core.publisher.Mono;

@Log4j2
public class CacheUtils {

    private static final String FIXED_KEY = "FIXED_KEY";

    private CacheUtils() {}

    public static <T> Function<String, T> of(@NotNull Duration duration, @NotNull Function<String, T> fn) {
        final LoadingCache<String, T> cache =
                Caffeine.newBuilder().expireAfterWrite(duration).build(fn::apply);

        return cache::get;
    }

    public static <T> Supplier<T> of(@NotNull Duration duration, @NotNull Supplier<T> supplier) {
        Function<String, T> fn = of(duration, k -> supplier.get());
        return () -> fn.apply(FIXED_KEY);
    }

    public static <T> Function<String, Mono<T>> ofMono(Duration duration, Function<String, Mono<T>> fn) {
        AsyncCache<String, T> cache =
                Caffeine.newBuilder().expireAfterWrite(duration).buildAsync();
        return key -> Mono.fromFuture(cache.get(key, k -> (T) fn.apply(k).toFuture()));
    }

    public static <T> Mono<T> ofMonoFixedKey(@NotNull Duration duration, @NotNull Mono<T> mono) {
        Function<String, Mono<T>> monoFn = ofMono(duration, key -> mono);
        return Mono.defer(() -> monoFn.apply(FIXED_KEY));
    }

    // TODO : close cache metrics to build test local
    public static <K, V> Cache<K, V> caffeine(
            @NotNull Duration duration,
            @NonNegative long maximumSize,
            @NotNull Class<?> cacheClass,
            @NonNull String cacheName) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .expireAfterWrite(duration)
                .recordStats()
                .maximumSize(maximumSize)
                .build();
        // CacheMetricsCollector cacheMetricsCollector =
        // ApplicationContextProvider.getApplicationContext().getBean(CacheMetricsCollector.class);
        // if(cacheMetricsCollector == null) {
        // log.error("Not found cacheMetricCollector");
        // } else {
        // cacheMetricsCollector.addCache(cacheName, cache);
        // }
        return cache;
    }
}
