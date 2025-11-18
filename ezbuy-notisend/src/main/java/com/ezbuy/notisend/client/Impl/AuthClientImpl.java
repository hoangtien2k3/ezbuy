package com.ezbuy.notisend.client.Impl;

import com.ezbuy.notisend.client.AuthClient;
import com.ezbuy.notisend.model.dto.response.ContactResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@DependsOn("webClientFactory")
public class AuthClientImpl implements AuthClient {

    private final WebClient auth;

    public AuthClientImpl(@Qualifier("auth") WebClient auth) {
        this.auth = auth;
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
}
