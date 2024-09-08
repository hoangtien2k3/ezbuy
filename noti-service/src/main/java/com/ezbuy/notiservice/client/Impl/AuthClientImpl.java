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
package com.ezbuy.notiservice.client.Impl;

import com.ezbuy.notiservice.client.AuthClient;
import com.fasterxml.jackson.core.type.TypeReference;
import io.hoangtien2k3.commons.client.BaseRestClient;
import io.hoangtien2k3.commons.utils.DataUtil;
import java.util.ArrayList;
import java.util.List;
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
public class AuthClientImpl implements AuthClient {

    private final BaseRestClient baseRestClient;

    @Qualifier(value = "auth")
    private final WebClient auth;

    @Override
    public Mono<List<String>> getAllUserId() {
        return baseRestClient
                .get(auth, "/auth/get-all", null, null, String.class)
                .map(response -> {
                    Optional<String> optionalS = (Optional<String>) response;
                    return DataUtil.parseStringToObject(
                            DataUtil.safeToString(optionalS.get()), new TypeReference<>() {}, new ArrayList<>());
                })
                .onErrorResume(throwable -> {
                    log.error("throwable: ", throwable);
                    return new ArrayList<>();
                });
    }
}
