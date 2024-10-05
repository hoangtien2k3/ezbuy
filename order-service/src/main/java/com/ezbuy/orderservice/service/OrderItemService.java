package com.ezbuy.orderservice.service;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.ordermodel.dto.request.ReviewOrderItemRequest;
import reactor.core.publisher.Mono;

public interface OrderItemService {

    Mono<DataResponse> review(ReviewOrderItemRequest request);
}
