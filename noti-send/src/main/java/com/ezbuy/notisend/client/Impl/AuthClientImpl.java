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
package com.ezbuy.notisend.client.Impl;

import com.ezbuy.notimodel.dto.response.ContactResponse;
import com.ezbuy.notisend.client.AuthClient;
import io.hoangtien2k3.commons.client.BaseRestClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class AuthClientImpl implements AuthClient {

    private final BaseRestClient baseRestClient;
    private final WebClient auth;

    @Override
    public Mono<ContactResponse> getContacts(List<String> userIds) {
        // return baseRestClient.post(auth, "/user/contacts", null, userIds,
        // ContactResponse.class)
        // .onErrorResume(throwable -> Optional.empty());
        return auth.method(HttpMethod.GET)
                .uri("/user/contacts")
                .bodyValue(userIds)
                .retrieve()
                .bodyToMono(ContactResponse.class)
                .onErrorResume(throwable -> {
                    log.info("Email list not found");
                    return Mono.just(new ContactResponse());
                });
    }
}
