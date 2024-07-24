package com.ezbuy.framework.annotations.cache;

import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

import com.ezbuy.framework.config.ApplicationContextProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheUtils {

    public static void invokeMethod(Method method) {
        try {
            Class declaringClass = method.getDeclaringClass();
            Object t = ApplicationContextProvider.getApplicationContext().getBean(declaringClass);
            Mono<Object> rs = (Mono<Object>) method.invoke(t);
            rs.subscribe();
        } catch (Exception exception) {
            log.error(
                    "Error when autoload cache " + method.getDeclaringClass().getSimpleName() + "." + method.getName(),
                    exception.getMessage(),
                    exception);
        }
    }
}
