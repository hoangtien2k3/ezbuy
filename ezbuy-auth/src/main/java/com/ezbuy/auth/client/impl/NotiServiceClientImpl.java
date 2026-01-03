package com.ezbuy.auth.client.impl;

import com.ezbuy.auth.model.request.CreateNotificationDTO;
import com.ezbuy.auth.client.NotiServiceClient;
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

    public NotiServiceClientImpl(@Qualifier("notification") WebClient notiServiceClient,
                                 BaseRestClient baseRestClient) {
        this.notiServiceClient = notiServiceClient;
        this.baseRestClient = baseRestClient;
    }

    @Override
    public Mono<Optional<DataResponse<Object>>> insertTransmission(CreateNotificationDTO createNotificationDTO) {
        return baseRestClient.post(
                notiServiceClient,
                "/v1/transmission/create-noti",
                null,
                createNotificationDTO,
                BaseRestClient.typeOf()
        );
    }
}
