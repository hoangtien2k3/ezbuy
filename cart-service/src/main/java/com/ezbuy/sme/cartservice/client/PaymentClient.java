package com.ezbuy.sme.cartservice.client;

import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface PaymentClient {
    Mono<Optional<Long>> estimatePrice(ProductPriceRequest productPriceRequest);
}
