package com.ezbuy.triggerservice.client.impl;

import com.ezbuy.triggerservice.client.ProductClient;
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
public class ProductClientImpl implements ProductClient {

    private final WebClient productClient;
    private final BaseRestClient restClient;

    public ProductClientImpl(@Qualifier("productClient") WebClient productClient,
                             BaseRestClient restClient) {
        this.productClient = productClient;
        this.restClient = restClient;
    }

    @Override
    public Mono<DataResponse> syncFilter() {
        return restClient
                .post(productClient, "/v1/sync-filter-template", null, null, DataResponse.class)
                .map(DataResponse::success);
    }

    @Override
    public Mono<DataResponse> syncSubscriber() {
        return restClient
                .post(productClient, "/v1/sync-subscriber", null, null, DataResponse.class)
                .map(DataResponse::success);
    }
}
