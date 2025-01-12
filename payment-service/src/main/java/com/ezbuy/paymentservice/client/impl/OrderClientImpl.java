package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.ordermodel.dto.request.UpdateOrderStateForOrderRequest;
import com.ezbuy.paymentmodel.constants.ClientUris;
import com.ezbuy.paymentservice.client.OrderClient;
import com.ezbuy.paymentservice.client.properties.OrderClientProperties;
import com.reactify.client.BaseRestClient;
import com.reactify.constants.MessageConstant;
import com.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@DependsOn("webClientFactory")
@RequiredArgsConstructor
public class OrderClientImpl implements OrderClient {
    private final BaseRestClient baseRestClient;
    @Qualifier("orderClient")
    private final WebClient orderClient;
    private final OrderClientProperties orderProperties;

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
