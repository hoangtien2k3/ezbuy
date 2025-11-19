package com.ezbuy.order.service;

import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.order.dto.request.SearchOrderRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface OrderService {

    Mono<DataResponse> searchOrder(SearchOrderRequest request);

    Mono<DataResponse> findDetail(String orderId);

    Mono<ResponseEntity<Resource>> getImportGroupMemberTemplate();
}
