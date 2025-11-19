package com.ezbuy.paymentservice.client;

import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface OrderClient {
    Mono<DataResponse> updateStatusOrder(String orderCode, Integer orderState);
}
