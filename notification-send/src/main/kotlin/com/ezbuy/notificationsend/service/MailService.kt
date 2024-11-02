package com.ezbuy.notificationsend.service

import com.ezbuy.notificationmodel.dto.EmailResultDTO
import com.ezbuy.notificationmodel.dto.TransmissionNotiDTO
import reactor.core.publisher.Mono

interface MailService {
    fun sendMailByTransmission(transmissionNotis: List<TransmissionNotiDTO>): Mono<List<EmailResultDTO>>
}