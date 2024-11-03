package com.ezbuy.notificationsend.client.impl

import com.ezbuy.notificationsend.client.AuthClient
import com.ezbuy.notificationmodel.dto.response.ContactResponse
import com.reactify.client.BaseRestClient
import lombok.extern.slf4j.Slf4j
import org.reflections.Reflections.log
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.DependsOn
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Slf4j
@Service
@DependsOn("webClientFactory")
class AuthClientImpl(
    private val baseRestClient: BaseRestClient<ContactResponse>,
    @Qualifier("auth") private val auth: WebClient
) : AuthClient {

    override fun getContacts(userIds: List<String>): Mono<ContactResponse> {
        // return baseRestClient.post(auth, "/user/contacts", null, userIds, ContactResponse::class.java)
        // .onErrorResume { Optional.empty() }
        return auth.method(HttpMethod.GET)
            .uri("/user/contacts")
            .bodyValue(userIds)
            .retrieve()
            .bodyToMono(ContactResponse::class.java)
            .onErrorResume { throwable ->
                log.info("Email list not found")
                Mono.just(ContactResponse())
            }
    }
}