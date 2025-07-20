package com.ezbuy.orderservice.client;

import com.ezbuy.ordermodel.dto.request.PricingProductRequest;
import com.ezbuy.ordermodel.dto.ws.PricingProductWSResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface PricingClient {
    Mono<Optional<PricingProductWSResponse>> getPricingProduct(PricingProductRequest request);
}
