package com.ezbuy.notiservice.client;

import reactor.core.publisher.Mono;

import java.util.List;

public interface AuthClient {
    Mono<List<String>> getAllUserId();
}