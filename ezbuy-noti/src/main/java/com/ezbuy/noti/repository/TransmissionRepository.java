package com.ezbuy.noti.repository;

import com.ezbuy.noti.model.dto.TransmissionNotiDTO;
import com.ezbuy.noti.model.dto.response.CountNoticeDTO;
import com.ezbuy.noti.model.entity.NotificationContent;
import com.ezbuy.noti.model.entity.Transmission;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static com.ezbuy.noti.repository.query.TransmissionQuery.*;

public interface TransmissionRepository extends R2dbcRepository<Transmission, String> {

    @Query(value = GET_COUNT_NOTICE_DTO)
    Flux<CountNoticeDTO> getListCountNoticeDTO(String receiver);

    @Query(value = GET_ALL_BY_NOTIFICATION_BY_CATEGORY_TYPE)
    Flux<String> getListTransId(String receiver, String notificationContentId);

    @Query(value = CHANGE_STATE_TRANSMISSION_BY_TYPE)
    Mono<Void> changeStateTransmissionByNotiIdAndReceiver(String state, String receiver, String notificationContentId);

    @Query(value = GET_ALL_NOTIFICATION_CONTENT_BY_CREATE_AT_AFTER)
    Flux<NotificationContent> getAllNotificationContentByCreateAtAfter(String receiver, LocalDateTime newestNotiTime);

    @Query(value = FIND_BY_ID_AND_STATUS)
    Mono<Transmission> findByIdAndStatus(String id, Integer status);

    @Query(value = GET_TRANSMISSIONS_TO_SEND_MAIL)
    Flux<TransmissionNotiDTO> getTransmissionsToSendMail(Integer resendCount);

    @Query(value = UPDATE_TRANSMISSION_STATE)
    Mono<Boolean> updateTransmissionSuccessState(List<String> transmissionIds);

    @Query(value = UPDATE_TRANSMISSION_STATE_AND_RESEND_COUNT)
    Mono<Boolean> updateTransmissionFailedState(List<String> transmissionIds);

    @Query(value = UPDATE_STATE_BY_ID)
    Mono<Boolean> updateTransmissionStateById(String id, String state);

    @Query(value = GET_TRANSMISSIONS_TO_SEND_MAIL)
    Flux<TransmissionNotiDTO> getTransmissionsToSendMail(Integer resendCount, Integer limit);

    @Query(value = UPDATE_TRANSMISSION_REST_STATE)
    Mono<Boolean> updateTransmissionRestSuccessState(List<String> transmissionIds);

    @Query(value = UPDATE_TRANSMISSION_EMAIL_STATE)
    Mono<Boolean> updateTransmissionEmailSuccessState(List<String> transmissionIds);

    @Query(value = FIND_BY_ID_AND_STATUS)
    Mono<Transmission> findByIdAndStatus(String id, int status);
}
