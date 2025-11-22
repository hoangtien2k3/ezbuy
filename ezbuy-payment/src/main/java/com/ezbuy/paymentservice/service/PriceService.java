package com.ezbuy.paymentservice.service;

import com.ezbuy.paymentservice.model.dto.request.ProductPriceRequest;
import com.ezbuy.paymentservice.model.dto.response.ProductPrice;
import reactor.core.publisher.Mono;

public interface PriceService {
    Mono<ProductPrice> calculatePrices(ProductPriceRequest productPriceRequest);
}
