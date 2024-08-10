package com.ezbuy.authservice.client.impl;

import com.ezbuy.authmodel.constants.UrlPaths;
import com.ezbuy.authservice.client.NotiServiceClient;
import com.ezbuy.framework.client.BaseRestClient;
import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

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
        String URL_CREATE_NOTI = UrlPaths.Noti.PREFIX + UrlPaths.Noti.CREATE_NOTI;
        return baseRestClient.post(
                notiServiceClient, URL_CREATE_NOTI, null, createNotificationDTO, DataResponse.class);
    }
}
