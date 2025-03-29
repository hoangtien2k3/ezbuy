package com.ezbuy.notificationservice.repository

import com.ezbuy.notificationservice.repository.query.TransmissionQuery
import com.ezbuy.notificationmodel.dto.TransmissionNotiDTO
import com.ezbuy.notificationmodel.dto.response.CountNoticeDTO
import com.ezbuy.notificationmodel.model.NotificationContent
import com.ezbuy.notificationmodel.model.Transmission
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface TransmissionRepository : R2dbcRepository<Transmission, String> {

    @Query(TransmissionQuery.GET_COUNT_NOTICE_DTO)
    fun getListCountNoticeDTO(receiver: String): Flux<CountNoticeDTO>

    @Query(TransmissionQuery.GET_ALL_BY_NOTIFICATION_BY_CATEGORY_TYPE)
    fun getListTransId(receiver: String, notificationContentId: String): Flux<String>

    @Query(TransmissionQuery.CHANGE_STATE_TRANSMISSION_BY_TYPE)
    fun changeStateTransmissionByNotiIdAndReceiver(state: String, receiver: String, notificationContentId: String): Mono<Void>

    @Query(TransmissionQuery.GET_ALL_NOTIFICATION_CONTENT_BY_CREATE_AT_AFTER)
    fun getAllNotificationContentByCreateAtAfter(receiver: String, newestNotiTime: LocalDateTime): Flux<NotificationContent>

    @Query(TransmissionQuery.FIND_BY_ID_AND_STATUS)
    fun findByIdAndStatus(id: String, status: Int): Mono<Transmission>

    @Query(TransmissionQuery.GET_TRANSMISSIONS_TO_SEND_MAIL)
    fun getTransmissionsToSendMail(resendCount: Int): Flux<TransmissionNotiDTO>

    @Query(TransmissionQuery.UPDATE_TRANSMISSION_STATE)
    fun updateTransmissionSuccessState(transmissionIds: List<String>): Mono<Boolean>

    @Query(TransmissionQuery.UPDATE_TRANSMISSION_STATE_AND_RESEND_COUNT)
    fun updateTransmissionFailedState(transmissionIds: List<String>): Mono<Boolean>

    @Query(TransmissionQuery.UPDATE_STATE_BY_ID)
    fun updateTransmissionStateById(id: String, state: String): Mono<Boolean>
}
