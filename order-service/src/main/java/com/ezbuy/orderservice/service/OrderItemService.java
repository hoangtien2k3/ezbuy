package com.ezbuy.orderservice.service;

import com.ezbuy.ordermodel.dto.request.ReviewOrderItemRequest;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface OrderItemService {

    Mono<DataResponse> review(ReviewOrderItemRequest request);
}
