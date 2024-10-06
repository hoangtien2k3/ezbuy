package com.ezbuy.productservice.client;

import com.ezbuy.ordermodel.dto.request.PricingProductRequest;
import com.ezbuy.ordermodel.dto.response.GetOrderReportResponse;
import com.ezbuy.ordermodel.dto.response.PricingProductResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;

public interface OrderClient {
    Mono<PricingProductResponse> getPricingProduct(PricingProductRequest productRequest);
    Mono<Optional<GetOrderReportResponse>> getOrderReport(LocalDate dateReport);
}
