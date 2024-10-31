package com.ezbuy.notificationsend.service

import com.ezbuy.notimodel.dto.EmailResultDTO
import com.ezbuy.notimodel.dto.TransmissionNotiDTO
import reactor.core.publisher.Mono

interface MailService {
    fun sendMailByTransmission(transmissionNotis: List<TransmissionNotiDTO>): Mono<List<EmailResultDTO>>
}