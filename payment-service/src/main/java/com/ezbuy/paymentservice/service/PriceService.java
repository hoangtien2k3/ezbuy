package com.ezbuy.paymentservice.service;

import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import com.ezbuy.paymentmodel.dto.response.ProductPrice;
import reactor.core.publisher.Mono;

public interface PriceService {
    Mono<ProductPrice> calculatePrices(ProductPriceRequest productPriceRequest);
}
