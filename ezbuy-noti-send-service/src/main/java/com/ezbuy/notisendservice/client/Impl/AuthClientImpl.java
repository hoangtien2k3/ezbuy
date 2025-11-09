package com.ezbuy.notisendservice.client.Impl;

import com.ezbuy.notimodel.dto.response.ContactResponse;
import com.ezbuy.notisendservice.client.AuthClient;
import com.ezbuy.core.client.BaseRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class AuthClientImpl implements AuthClient {

    @Qualifier("auth")
    private final WebClient auth;
    private final BaseRestClient baseRestClient;

    @Override
    public Mono<ContactResponse> getContacts(List<String> userIds) {
        return auth.method(HttpMethod.GET).uri("/user/contacts")
                .bodyValue(userIds)
                .retrieve()
                .bodyToMono(ContactResponse.class)
                .onErrorResume(throwable -> {
                    log.info("Email list not found");
                    return Mono.just(new ContactResponse());
                });
    }
}
