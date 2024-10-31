package com.ezbuy.notificationsend.repository

import com.ezbuy.notificationsend.repository.query.TransmissionQuery
import com.ezbuy.notimodel.dto.TransmissionNotiDTO
import com.ezbuy.notimodel.model.Transmission
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface TransmissionRepository : R2dbcRepository<Transmission, String> {
    @Query(TransmissionQuery.GET_TRANSMISSIONS_TO_SEND_MAIL)
    fun getTransmissionsToSendMail(resendCount: Int?, limit: Int?): Flux<TransmissionNotiDTO>

    @Query(TransmissionQuery.UPDATE_TRANSMISSION_REST_STATE)
    fun updateTransmissionRestSuccessState(transmissionIds: List<String>): Mono<Boolean>

    @Query(TransmissionQuery.UPDATE_TRANSMISSION_EMAIL_STATE)
    fun updateTransmissionEmailSuccessState(transmissionIds: List<String>): Mono<Boolean>

    @Query(TransmissionQuery.UPDATE_TRANSMISSION_STATE_AND_RESEND_COUNT)
    fun updateTransmissionFailedState(transmissionIds: List<String>): Mono<Boolean>

    @Query(TransmissionQuery.UPDATE_STATE_BY_ID)
    fun updateTransmissionStateById(id: String, state: String): Mono<Boolean>

    @Query(TransmissionQuery.FIND_BY_ID_AND_STATUS)
    fun findByIdAndStatus(id: String, status: Int): Mono<Transmission>

    @Query(TransmissionQuery.GET_TRANSMISSION_BY_NOTIFICATION_CONTENT_ID)
    fun getListTransId(receiver: String, notificationContentId: String): Flux<String>

    @Query(TransmissionQuery.CHANGE_STATE_TRANSMISSION_BY_TYPE)
    fun changeStateTransmissionByNotiIdAndReceiver(state: String, receiver: String, notificationContentId: String): Mono<Void>
}
