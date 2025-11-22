package com.ezbuy.triggerservice.client.impl;

import com.ezbuy.triggerservice.client.OrderClient;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.triggerservice.dto.SyncOrderStateRequest;
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
    private final BaseRestClient restClient;

    public OrderClientImpl(@Qualifier("orderClient") WebClient orderClient,
                           BaseRestClient restClient) {
        this.orderClient = orderClient;
        this.restClient = restClient;
    }

    @Override
    public Mono<DataResponse> syncOrderState(SyncOrderStateRequest request) {
        return restClient
                .post(orderClient, "/v1/order/sync-order", null, request, DataResponse.class)
                .map(DataResponse::success);
    }
}
