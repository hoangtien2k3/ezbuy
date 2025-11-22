package com.ezbuy.triggerservice.client;

import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface ProductClient {

    Mono<DataResponse> syncFilter();

    Mono<DataResponse> syncSubscriber();
}
