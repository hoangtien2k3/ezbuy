package com.ezbuy.notificationservice.client.impl

import com.ezbuy.notificationservice.client.AuthClient
import com.fasterxml.jackson.core.type.TypeReference
import com.reactify.client.BaseRestClient
import com.reactify.util.DataUtil
import com.reactify.util.SecurityUtils
import lombok.extern.slf4j.Slf4j
import org.reflections.Reflections.log
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.DependsOn
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*

@Slf4j
@Service
@DependsOn("webClientFactory")
class AuthClientImpl(
    private val baseRestClient: BaseRestClient<String>,
    @Qualifier("auth") private val auth: WebClient
) : AuthClient {

    override fun getAllUserId(): Mono<List<String>> {
        return baseRestClient
            .get(auth, "/auth/get-all", null, null, String::class.java)
            .map { response ->
                val optionalS = response as? Optional<String>
                DataUtil.parseStringToObject(
                    DataUtil.safeToString(optionalS?.get()),
                    object : TypeReference<List<String>>() {},
                    ArrayList()
                )
            }
            .onErrorResume { throwable ->
                log.error("throwable: $throwable")
                Mono.just(ArrayList())
            }
    }

    override fun getEmailsByUsername(username: String): Mono<String> {
        val payload = LinkedMultiValueMap<String, String>().apply {
            set("username", username)
        }
        return SecurityUtils.getTokenUser().flatMap { token ->
            val headers = HttpHeaders().apply {
                set("Authorization", "Bearer $token")
            }
            baseRestClient
                .get(auth, "/user/keycloak", headers, payload, String::class.java)
                .map { response ->
                    DataUtil.safeToString((response as? Optional<*>)?.orElse(null))
                }
        }
    }

}
