package com.ezbuy.cartservice.infrastructure.client;

import java.util.Optional;

import com.ezbuy.cartservice.application.dto.request.ProductPriceRequest;
import reactor.core.publisher.Mono;

public interface PaymentClient {
    Mono<Optional<Long>> estimatePrice(ProductPriceRequest productPriceRequest);
}
