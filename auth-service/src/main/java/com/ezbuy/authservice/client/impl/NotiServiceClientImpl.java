package com.ezbuy.authservice.client.impl;

import com.ezbuy.authmodel.constants.UrlPaths;
import com.ezbuy.authservice.client.NotiServiceClient;
import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import com.reactify.client.BaseRestClient;
import com.reactify.model.response.DataResponse;
import com.reactify.util.SecurityUtils;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

//        return SecurityUtils.getTokenUser()
//                .flatMap(token -> {
//                    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//                    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
//                    return baseRestClient
//                            .post(
//                                    notiServiceClient,
//                                    UrlPaths.Noti.CREATE_NOTI,
//                                    headers,
//                                    createNotificationDTO,
//                                    DataResponse.class)
//                            .doOnSuccess(result -> log.info("Call noti-service insert transmission result: {}", result))
//                            .doOnError(error -> log.error("Error calling noti-service: ", error));
//                });
    }
}
