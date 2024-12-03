package com.ezbuy.notificationservice.client

import reactor.core.publisher.Mono

interface AuthClient {
    fun getAllUserId(): Mono<List<String>>

    fun getEmailsByUsername(username: String): Mono<String>
}
