package com.ezbuy.notificationsend.service

import com.ezbuy.reactify.model.response.DataResponse
import reactor.core.publisher.Mono

interface TransmissionService {
    fun sendNotification(): Mono<DataResponse<Any>>
}