package com.ezbuy.notificationservice.client.impl

import com.ezbuy.notificationservice.client.AuthClient
import com.fasterxml.jackson.core.type.TypeReference
import io.hoangtien2k3.reactify.DataUtil
import io.hoangtien2k3.reactify.client.BaseRestClient
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.reflections.Reflections.log
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*
import kotlin.collections.ArrayList

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
}
