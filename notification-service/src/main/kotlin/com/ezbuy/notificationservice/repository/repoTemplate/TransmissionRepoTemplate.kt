package com.ezbuy.notificationservice.repository.repoTemplate

import com.ezbuy.notificationmodel.dto.UserTransmissionDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface TransmissionRepoTemplate {
    fun searchUserTransmission(
        email: String?,
        templateMail: String?,
        from: LocalDateTime?,
        to: LocalDateTime?,
        offset: Int,
        limit: Int,
        sort: String
    ): Flux<UserTransmissionDTO>

    fun countUserTransmission(
        email: String?,
        templateMail: String?,
        from: LocalDateTime?,
        to: LocalDateTime?
    ): Mono<Long>
}
