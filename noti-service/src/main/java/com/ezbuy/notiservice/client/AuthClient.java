package com.ezbuy.notiservice.client;

import java.util.List;
import reactor.core.publisher.Mono;

public interface AuthClient {
    Mono<List<String>> getAllUserId();
}
