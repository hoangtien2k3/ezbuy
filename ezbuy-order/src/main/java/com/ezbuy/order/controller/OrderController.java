package com.ezbuy.order.controller;

import com.ezbuy.order.dto.request.SearchOrderRequest;
import com.ezbuy.order.service.OrderService;
import com.ezbuy.core.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> searchOrder(SearchOrderRequest request) {
        return orderService.searchOrder(request).map(ResponseEntity::ok);
    }

    @GetMapping("/detail/{orderId}")
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> findDetail(@PathVariable("orderId") String orderId) {
        return orderService.findDetail(orderId).map(ResponseEntity::ok);
    }
}
