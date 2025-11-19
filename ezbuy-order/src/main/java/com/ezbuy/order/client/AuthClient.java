package com.ezbuy.order.client;

import reactor.core.publisher.Mono;

public interface AuthClient {

    Mono<String> getEmailsByUsername(String username);
}
