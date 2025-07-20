package com.ezbuy.triggerservice.client;

import com.ezbuy.ordermodel.dto.request.SyncOrderStateRequest;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface OrderClient {

    Mono<DataResponse> syncOrderState(SyncOrderStateRequest request);
}
