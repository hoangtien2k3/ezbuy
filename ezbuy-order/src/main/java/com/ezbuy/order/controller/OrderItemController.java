package com.ezbuy.order.controller;

import com.ezbuy.order.dto.request.ReviewOrderItemRequest;
import com.ezbuy.order.service.OrderItemService;
import com.ezbuy.core.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order-item")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping("/review")
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> review(@RequestBody ReviewOrderItemRequest request) {
        return orderItemService.review(request).map(ResponseEntity::ok);
    }
}
