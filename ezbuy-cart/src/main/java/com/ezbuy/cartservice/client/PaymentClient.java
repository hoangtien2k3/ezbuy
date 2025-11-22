package com.ezbuy.cartservice.client;

import java.util.Optional;

import com.ezbuy.cartservice.domain.dto.request.ProductPriceRequest;
import reactor.core.publisher.Mono;

public interface PaymentClient {
    Mono<Optional<Long>> estimatePrice(ProductPriceRequest productPriceRequest);
}
