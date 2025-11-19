package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.ordermodel.dto.request.UpdateOrderStateForOrderRequest;
import com.ezbuy.paymentservice.client.OrderClient;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.model.response.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@DependsOn("webClientFactory")
public class OrderClientImpl implements OrderClient {

    private final WebClient orderClient;
    private final BaseRestClient baseRestClient;

    public OrderClientImpl(@Qualifier("orderClient") WebClient orderClient,
                           BaseRestClient baseRestClient) {
        this.orderClient = orderClient;
        this.baseRestClient = baseRestClient;
    }

    @Override
    public Mono<DataResponse> updateStatusOrder(String orderCode, Integer orderState) {
        UpdateOrderStateForOrderRequest orderStatus = new UpdateOrderStateForOrderRequest();
        orderStatus.setOrderCode(orderCode);
        orderStatus.setPaymentStatus(orderState);
        return baseRestClient
                .post(orderClient, "/v1/order/payment-result", null, orderStatus, DataResponse.class)
                .map(response -> {
                    log.info("Update order status response: {}", response);
                    return new DataResponse<>("success", response);
                });
    }
}
