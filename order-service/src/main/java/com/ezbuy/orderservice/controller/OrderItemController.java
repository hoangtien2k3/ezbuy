package com.ezbuy.orderservice.controller;

import com.ezbuy.orderservice.service.OrderItemService;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.ordermodel.constants.UrlPaths;
import com.ezbuy.sme.ordermodel.dto.request.ReviewOrderItemRequest;
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
@RequestMapping(UrlPaths.OrderItem.PRE_FIX)
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping(UrlPaths.OrderItem.REVIEW)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse>> review(@RequestBody ReviewOrderItemRequest request) {
        return orderItemService.review(request)
                .map(ResponseEntity::ok);
    }
}
