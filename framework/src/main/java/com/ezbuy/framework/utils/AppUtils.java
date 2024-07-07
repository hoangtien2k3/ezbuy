package com.ezbuy.framework.utils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
public class AppUtils {

    public static void runHiddenStream(Mono functionMono) {
        functionMono.subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofMinutes(2))
                .doOnError(e -> log.error("runHiddenStream ex: ", e))
                .subscribe();
    }

    public static void runHiddenStreamTimeout(Mono functionMono, int timeout) {
        functionMono.subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofMinutes(timeout))
                .doOnError(e -> log.error("runHiddenStream ex: ", e))
                .subscribe();
    }

    public static void runHiddenStreamWithoutTimeout(Mono functionMono) {
        functionMono.subscribeOn(Schedulers.boundedElastic())
                .doOnError(e -> log.error("runHiddenStream ex: ", e))
                .subscribe();
    }

    public static Mono<Boolean> insertData(Mono functionMono) {
        return functionMono
                .map(rs -> true)
                .switchIfEmpty(Mono.just(true))
                .onErrorResume(throwable -> Mono.just(false));
    }

}
