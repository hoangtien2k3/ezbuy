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
            Class<?> declaringClass = method.getDeclaringClass();
            Object bean = ApplicationContextProvider.getApplicationContext().getBean(declaringClass);
            Mono<Object> rs = (Mono<Object>) method.invoke(bean);
            rs.subscribe();
        } catch (Exception exception) {
            log.error("Error when autoload cache " + method.getDeclaringClass().getSimpleName() + "." + method.getName(), exception.getMessage(), exception);
        }
    }

//    public static RedisTemplate<Object, Object> getRedisCache2lTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        serializer.setObjectMapper(objectMapper);
//        template.setKeySerializer(serializer);
//        template.setValueSerializer(serializer);
//        template.setValueSerializer(serializer);
//        return template;
//    }
}
