package com.ezbuy.paymentservice.client;

import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface OrderClient {
    Mono<DataResponse> updateStatusOrder(String orderCode, Integer orderState);
}
