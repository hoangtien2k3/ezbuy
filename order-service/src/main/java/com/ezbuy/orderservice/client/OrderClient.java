package com.ezbuy.orderservice.client;

import com.ezbuy.ordermodel.dto.ExtKeyDTO;
import com.ezbuy.ordermodel.dto.request.PricingProductRequest;
import com.ezbuy.ordermodel.dto.ws.PlaceOrderResponse;
import com.ezbuy.ordermodel.dto.ws.PricingProductWSResponse;
import com.ezbuy.ordermodel.dto.ws.SearchOrderStateResponse;
import com.ezbuy.ordermodel.dto.ws.ValidateDataOrderResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface OrderClient {

    Mono<Optional<PlaceOrderResponse>> placeOrder(String type, String dataJson);

    Mono<Optional<SearchOrderStateResponse>> searchOrderState(List<String> orderCodeList);

    Mono<Optional<PricingProductWSResponse>> getPricingProduct(PricingProductRequest request);

    Mono<Optional<ValidateDataOrderResponse>> validateDataOrder(String orderType, String dataJson, List<ExtKeyDTO> lstExtKey);
}
