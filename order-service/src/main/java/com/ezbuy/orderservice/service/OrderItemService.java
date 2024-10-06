package com.ezbuy.orderservice.service;

import com.ezbuy.ordermodel.dto.request.ReviewOrderItemRequest;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface OrderItemService {

    Mono<DataResponse> review(ReviewOrderItemRequest request);
}
