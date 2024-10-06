package com.viettel.sme.cartservice.client;

import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface PaymentClient {
    Mono<Optional<Long>> estimatePrice(ProductPriceRequest productPriceRequest);
}
