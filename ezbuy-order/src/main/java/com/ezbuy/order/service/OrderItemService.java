package com.ezbuy.order.service;

import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.order.dto.request.ReviewOrderItemRequest;
import reactor.core.publisher.Mono;

public interface OrderItemService {

    Mono<DataResponse> review(ReviewOrderItemRequest request);
}
