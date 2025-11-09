package com.ezbuy.notiservice.client.Impl;

import com.ezbuy.notiservice.client.AuthClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.util.DataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class AuthClientImpl implements AuthClient {

    @Qualifier(value = "auth")
    private final WebClient auth;
    private final BaseRestClient baseRestClient;

    @Override
    public Mono<List<String>> getAllUserId() {
        return baseRestClient
                .get(auth, "/auth/get-all", null, null, String.class)
                .map(response -> {
                    Optional<String> optionalS = (Optional<String>) response;
                    return DataUtil.parseStringToObject(DataUtil.safeToString(optionalS.get()), new TypeReference<>() {
                    }, new ArrayList<>());
                })
                .onErrorResume(throwable -> {
                    log.error("throwable: ", throwable);
                    return new ArrayList<>();
                });
    }

    @Override
    public Mono<String> getEmailsByUsername(String username) {
        var payload = new LinkedMultiValueMap<>();
        payload.set("username", username);
        return baseRestClient
                .get(auth, "/user/keycloak", null, payload, String.class)
                .map(response -> {
                    Object email = ((Optional<Object>) response).get();
                    return DataUtil.safeToString(email);
                });
    }
}
