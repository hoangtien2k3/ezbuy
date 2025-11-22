package com.ezbuy.triggerservice.client;

import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface SettingClient {
    Mono<DataResponse> syncNews();

    Mono<DataResponse> syncServices();
}
