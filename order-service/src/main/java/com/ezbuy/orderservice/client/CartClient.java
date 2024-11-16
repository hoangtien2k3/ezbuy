package com.ezbuy.orderservice.client;

import com.reactify.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface CartClient {
    Mono<DataResponse> clearAllCartItem(String userId, List<String> templateIds);
}
