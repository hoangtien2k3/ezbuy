package com.ezbuy.framework.annotations.cache;

import java.lang.reflect.Method;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CustomizeRemovalListener implements RemovalListener {
    private Method method;

    @Override
    public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause removalCause) {
        if (removalCause.wasEvicted()) {
            log.info("Cache "
                    + method.getDeclaringClass().getSimpleName()
                    + "."
                    + method.getName()
                    + " was evicted because "
                    + removalCause);
            CacheUtils.invokeMethod(method);
        }
    }
}
