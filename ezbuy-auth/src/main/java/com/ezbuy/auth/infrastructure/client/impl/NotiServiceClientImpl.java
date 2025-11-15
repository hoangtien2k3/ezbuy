package com.ezbuy.auth.infrastructure.client.impl;

import com.ezbuy.auth.infrastructure.client.NotiServiceClient;
import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.model.response.DataResponse;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@DependsOn("webClientFactory")
public class NotiServiceClientImpl implements NotiServiceClient {

    private final WebClient notiServiceClient;
    private final BaseRestClient baseRestClient;

    public NotiServiceClientImpl(@Qualifier("notiServiceClient") WebClient notiServiceClient,
                                 BaseRestClient baseRestClient) {
        this.notiServiceClient = notiServiceClient;
        this.baseRestClient = baseRestClient;
    }

    @Override
    public Mono<Optional<DataResponse<Object>>> insertTransmission(CreateNotificationDTO createNotificationDTO) {
        return baseRestClient.post(notiServiceClient, "/v1/transmission/create-noti", null, createNotificationDTO, BaseRestClient.typeOf());
    }
}
