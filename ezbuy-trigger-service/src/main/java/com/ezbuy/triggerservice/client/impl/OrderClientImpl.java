package com.ezbuy.triggerservice.client.impl;

import com.ezbuy.ordermodel.dto.request.SyncOrderStateRequest;
import com.ezbuy.triggerservice.client.OrderClient;
import com.reactify.client.BaseRestClient;
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
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class OrderClientImpl implements OrderClient {

    @Qualifier("orderClient")
    private final WebClient orderClient;

    private final BaseRestClient restClient;

    @Override
    public Mono<DataResponse> syncOrderState(SyncOrderStateRequest request) {
        return restClient
                .post(orderClient, "/v1/order/sync-order", null, request, DataResponse.class)
                .map(DataResponse::success);
    }
}
