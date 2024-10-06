package com.ezbuy.paymentservice.client;

import com.viettel.sme.framework.model.response.DataResponse;
import com.viettel.sme.ordermodel.dto.ws.PlaceOrderResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;
public interface OrderClient {
    Mono<DataResponse> updateStatusOrder(String orderCode, Integer orderState);
}
