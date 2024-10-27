package com.ezbuy.paymentservice.controller;

import com.ezbuy.paymentservice.service.PriceService;
import com.ezbuy.paymentmodel.constants.UrlPaths;
import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(UrlPaths.Price.PREFIX)
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    @PostMapping("/calculate")
    public Mono<ResponseEntity<DataResponse>> estimatePrice(@RequestBody ProductPriceRequest productPriceRequest) {
        return priceService.calculatePrices(productPriceRequest)
                .map(rs -> ResponseEntity.ok(new DataResponse("common.success", rs)));
    }
}
