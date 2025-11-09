package com.ezbuy.orderservice.client;

import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface CartClient {
    Mono<DataResponse> clearAllCartItem(String userId, List<String> templateIds);
}
