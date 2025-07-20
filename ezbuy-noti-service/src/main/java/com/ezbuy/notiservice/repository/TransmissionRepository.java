package com.ezbuy.notiservice.repository;

import com.ezbuy.notimodel.dto.TransmissionNotiDTO;
import com.ezbuy.notimodel.dto.response.CountNoticeDTO;
import com.ezbuy.notimodel.model.NotificationContent;
import com.ezbuy.notimodel.model.Transmission;
import com.ezbuy.notiservice.repository.query.TransmissionQuery;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface TransmissionRepository extends R2dbcRepository<Transmission, String> {

    @Query(TransmissionQuery.GET_COUNT_NOTICE_DTO)
    Flux<CountNoticeDTO> getListCountNoticeDTO(String receiver);

    @Query(TransmissionQuery.GET_ALL_BY_NOTIFICATION_BY_CATEGORY_TYPE)
    Flux<String> getListTransId(String receiver, String notificationContentId);

    @Query(TransmissionQuery.CHANGE_STATE_TRANSMISSION_BY_TYPE)
    Mono<Void> changeStateTransmissionByNotiIdAndReceiver(String state, String receiver, String notificationContentId);

    @Query(TransmissionQuery.GET_ALL_NOTIFICATION_CONTENT_BY_CREATE_AT_AFTER)
    Flux<NotificationContent> getAllNotificationContentByCreateAtAfter(String receiver, LocalDateTime newestNotiTime);

    @Query(TransmissionQuery.FIND_BY_ID_AND_STATUS)
    Mono<Transmission> findByIdAndStatus(String id, Integer status);

    @Query(TransmissionQuery.GET_TRANSMISSIONS_TO_SEND_MAIL)
    Flux<TransmissionNotiDTO> getTransmissionsToSendMail(Integer resendCount);

    @Query(TransmissionQuery.UPDATE_TRANSMISSION_STATE)
    Mono<Boolean> updateTransmissionSuccessState(List<String> transmissionIds);

    @Query(TransmissionQuery.UPDATE_TRANSMISSION_STATE_AND_RESEND_COUNT)
    Mono<Boolean> updateTransmissionFailedState(List<String> transmissionIds);

    @Query(TransmissionQuery.UPDATE_STATE_BY_ID)
    Mono<Boolean> updateTransmissionStateById(String id, String state);
}
