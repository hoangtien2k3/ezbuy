package com.ezbuy.order.client.impl;

import com.ezbuy.order.client.AuthClient;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SecurityUtils;

import java.util.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@DependsOn("webClientFactory")
public class AuthClientImpl implements AuthClient {

    private final WebClient authClient;
    private final BaseRestClient baseRestClient;

    public AuthClientImpl(@Qualifier("authClient") WebClient authClient,
                          BaseRestClient baseRestClient) {
        this.authClient = authClient;
        this.baseRestClient = baseRestClient;
    }

    @Override
    public Mono<String> getEmailsByUsername(String username) {
        LinkedMultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
        payload.set("username", username);
        return SecurityUtils.getTokenUser().flatMap(token -> {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            return baseRestClient
                    .get(authClient, "/user/keycloak", headers, payload, String.class)
                    .map(response -> DataUtil.safeToString(((Optional<?>) response).orElse(null)));
        });
    }
}
