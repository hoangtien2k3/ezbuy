package com.ezbuy.triggerservice.client;

import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.triggerservice.dto.SyncOrderStateRequest;
import reactor.core.publisher.Mono;

public interface OrderClient {

    Mono<DataResponse> syncOrderState(SyncOrderStateRequest request);
}
