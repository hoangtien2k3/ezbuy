package com.ezbuy.triggerservice.client.impl;

import com.ezbuy.triggerservice.client.SettingClient;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.model.response.DataResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@DependsOn("webClientFactory")
public class SettingClientImpl implements SettingClient {

    private final WebClient settingClient;
    private final BaseRestClient baseRestClient;

    public SettingClientImpl(@Qualifier("settingClient") WebClient settingClient,
                             BaseRestClient baseRestClient) {
        this.settingClient = settingClient;
        this.baseRestClient = baseRestClient;
    }

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
