package com.ezbuy.orderservice.client;

import io.hoangtien2k3.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CartClient {
    Mono<DataResponse> clearAllCartItem(String userId, List<String> templateIds);
}
