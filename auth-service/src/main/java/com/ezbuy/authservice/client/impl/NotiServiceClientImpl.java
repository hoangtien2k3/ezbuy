/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.authservice.client.impl;

import com.ezbuy.authmodel.constants.UrlPaths;
import com.ezbuy.authservice.client.NotiServiceClient;
import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import io.hoangtien2k3.commons.client.BaseRestClient;
import io.hoangtien2k3.commons.model.response.DataResponse;
import io.hoangtien2k3.commons.utils.SecurityUtils;
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
        return SecurityUtils.getTokenUser().flatMap(token -> {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            return baseRestClient
                    .post(
                            notiServiceClient,
                            UrlPaths.Noti.CREATE_NOTI,
                            headers,
                            createNotificationDTO,
                            DataResponse.class)
                    .doOnSuccess(result -> log.info("Call noti-service insert transmission result: {}", result))
                    .doOnError(error -> log.error("Error calling noti-service: ", error));
        });
    }
}
