package com.ezbuy.triggerservice.client.impl;

import com.ezbuy.triggerservice.client.SettingClient;
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
public class SettingClientImpl implements SettingClient {

    @Qualifier("settingClient")
    private final WebClient settingClient;

    private final BaseRestClient baseRestClient;

    @Override
    public Mono<DataResponse> syncNews() {
        return baseRestClient
                .post(settingClient, "/v1/global-search/news", null, null, DataResponse.class)
                .map(DataResponse::success);
    }

    @Override
    public Mono<DataResponse> syncServices() {
        return baseRestClient
                .post(settingClient, "/v1/global-search/services", null, null, DataResponse.class)
                .map(DataResponse::success);
    }
}
