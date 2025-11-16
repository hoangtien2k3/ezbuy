/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.core.cache;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import java.lang.reflect.Method;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A custom removal listener for handling cache eviction events.
 * <p>
 * This listener logs when a cache entry is evicted and optionally invokes a
 * predefined method upon eviction.
 * </p>
 *
 * @author hoangtien2k3
 */
@RequiredArgsConstructor
public class CacheRemovalListener implements RemovalListener<Object, Object> {

    private static final Logger log = LoggerFactory.getLogger(CacheRemovalListener.class);

    private final Method method;

    @Override
    public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause removalCause) {
        if (!removalCause.wasEvicted()) {
            return;
        }
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        log.info("Cache {} was evicted due to {}", methodName, removalCause);
        CacheUtils.invokeMethod(method);
    }
}
