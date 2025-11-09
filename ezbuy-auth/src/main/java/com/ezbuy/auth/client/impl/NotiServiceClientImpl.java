package com.ezbuy.auth.client.impl;

import com.ezbuy.auth.client.NotiServiceClient;
import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.model.response.DataResponse;

import java.util.Optional;

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
public class NotiServiceClientImpl implements NotiServiceClient {

    @Qualifier("notiServiceClient")
    private final WebClient notiServiceClient;
    private final BaseRestClient<DataResponse> baseRestClient;

    @Override
    public Mono<Optional<DataResponse>> insertTransmission(CreateNotificationDTO createNotificationDTO) {
        return baseRestClient.post(notiServiceClient, "/v1/transmission/create-noti", null, createNotificationDTO, DataResponse.class);
    }
}
