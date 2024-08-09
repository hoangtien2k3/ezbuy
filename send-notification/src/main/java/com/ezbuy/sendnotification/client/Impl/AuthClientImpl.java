package com.ezbuy.sendnotification.client.Impl;

import com.ezbuy.framework.client.BaseRestClient;
import com.ezbuy.sendnotification.client.AuthClient;
import com.ezbuy.sendnotification.model.noti.ContactResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final BaseRestClient baseRestClient;
    private final WebClient auth;

    @Override
    public Mono<ContactResponse> getContacts(List<String> userIds) {
//        return baseRestClient.post(auth, "/user/contacts", null, userIds, ContactResponse.class)
//                .onErrorResume(throwable -> Optional.empty());
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
