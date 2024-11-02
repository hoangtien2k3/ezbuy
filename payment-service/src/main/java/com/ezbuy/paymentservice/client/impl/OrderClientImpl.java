package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.ordermodel.dto.request.UpdateOrderStateForOrderRequest;
import com.ezbuy.paymentmodel.constants.ClientUris;
import com.ezbuy.paymentservice.client.OrderClient;
import com.ezbuy.paymentservice.client.properties.OrderClientProperties;
import io.hoangtien2k3.reactify.client.BaseRestClient;
import io.hoangtien2k3.reactify.constants.MessageConstant;
import io.hoangtien2k3.reactify.model.response.DataResponse;
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
    private final BaseRestClient baseRestClient;
    private final WebClient orderClient;
    private final OrderClientProperties orderProperties;

    public OrderClientImpl(
            BaseRestClient baseRestClient,
            @Qualifier("orderClient") WebClient orderClient,
            OrderClientProperties orderProperties) {
        this.baseRestClient = baseRestClient;
        this.orderClient = orderClient;
        this.orderProperties = orderProperties;
    }

    @Override
    public Mono<DataResponse> updateStatusOrder(String orderCode, Integer orderState) {
        UpdateOrderStateForOrderRequest updateOrderStateForOrderRequest = new UpdateOrderStateForOrderRequest();
        updateOrderStateForOrderRequest.setOrderCode(orderCode);
        updateOrderStateForOrderRequest.setPaymentStatus(orderState);
        return baseRestClient
                .post(
                        orderClient,
                        ClientUris.Order.UPDATE_PAYMENT_RESULT,
                        null,
                        updateOrderStateForOrderRequest,
                        DataResponse.class)
                .map(response -> {
                    log.info("CM response ", response);
                    return new DataResponse<>(MessageConstant.SUCCESS, null);
                })
                .onErrorResume(throwable -> {
                    log.error("call api updateOrderState error: {}", throwable);
                    return Mono.just(new DataResponse<>(MessageConstant.SUCCESS, null));
                });
    }
}
