package com.ezbuy.orderservice.client;

import com.ezbuy.ordermodel.dto.request.PricingProductRequest;
import com.ezbuy.ordermodel.dto.ws.PricingProductWSResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface PricingClient {
    Mono<Optional<PricingProductWSResponse>> getPricingProduct(PricingProductRequest request);
}
