package com.ezbuy.notificationsend.service

import io.hoangtien2k3.reactify.model.response.DataResponse
import reactor.core.publisher.Mono

interface TransmissionService {
    fun sendNotification(): Mono<DataResponse<Any>>
}