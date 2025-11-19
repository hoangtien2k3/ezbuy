package com.ezbuy.paymentservice.controller;

import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import com.ezbuy.paymentservice.service.PriceService;
import com.ezbuy.core.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/price")
public class PriceController {
    
    private final PriceService priceService;

    @PostMapping("/calculate")
    public Mono<ResponseEntity<DataResponse>> estimatePrice(@RequestBody ProductPriceRequest productPriceRequest) {
        return priceService
                .calculatePrices(productPriceRequest)
                .map(rs -> ResponseEntity.ok(new DataResponse("common.success", rs)));
    }
}
