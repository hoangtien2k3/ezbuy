package com.ezbuy.paymentservice.client;

import io.hoangtien2k3.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface OrderClient {
    Mono<DataResponse> updateStatusOrder(String orderCode, Integer orderState);
}
