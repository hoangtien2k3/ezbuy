package com.ezbuy.noti.client.Impl;

import com.ezbuy.noti.client.AuthClient;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.noti.model.dto.response.ContactResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@DependsOn("webClientFactory")
public class AuthClientImpl implements AuthClient {

    private final WebClient auth;
    private final BaseRestClient baseRestClient;

    public AuthClientImpl(@Qualifier(value = "auth") WebClient auth,
                          BaseRestClient baseRestClient) {
        this.auth = auth;
        this.baseRestClient = baseRestClient;
    }

    @Override
    public Mono<ContactResponse> getContacts(List<String> userIds) {
        return auth.method(HttpMethod.GET)
                .uri("/user/contacts")
                .bodyValue(userIds)
                .retrieve()
                .bodyToMono(ContactResponse.class)
                .onErrorResume(throwable -> Mono.just(new ContactResponse()));
    }

    @Override
    public Mono<List<String>> getAllUserId() {
        ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<>() {};
        return baseRestClient
                .get(auth, "/auth/get-all", null, null, typeRef)
                .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty));
    }

    @Override
    public Mono<String> getEmailsByUsername(String username) {
        var payload = new LinkedMultiValueMap<String, String>();
        payload.set("username", username);
        return baseRestClient
                .get(auth, "/user/keycloak", null, payload, String.class)
                .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty));
    }
}
