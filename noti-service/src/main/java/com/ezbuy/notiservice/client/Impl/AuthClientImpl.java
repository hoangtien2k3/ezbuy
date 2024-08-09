package com.ezbuy.notiservice.client.Impl;

import com.ezbuy.framework.client.BaseRestClient;
import com.ezbuy.framework.utils.DataUtil;
import com.ezbuy.notiservice.client.AuthClient;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
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

    private final BaseRestClient baseRestClient;
    @Qualifier(value = "auth")
    private final WebClient auth;

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
}
