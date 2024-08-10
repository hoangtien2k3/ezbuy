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
    @Query(value = TransmissionQuery.getCountNoticeDTO)
    Flux<CountNoticeDTO> getListCountNoticeDTO(String receiver);

    @Query(value = TransmissionQuery.getTransmissionByNotificationContentId)
    Flux<String> getListTransId(String receiver, String notificationContentId);

    @Query(value = TransmissionQuery.changeStateTransmissionByType)
    Mono<Void> changeStateTransmissionByNotiIdAndReceiver(String state, String receiver, String notificationContentId);

    @Query(value = TransmissionQuery.getAllNotificationContentByCreateAtAfter)
    Flux<NotificationContent> getAllNotificationContentByCreateAtAfter(String receiver, LocalDateTime newestNotiTime);

    @Query(value = TransmissionQuery.findByIdAndStatus)
    Mono<Transmission> findByIdAndStatus(String id, int status);

    @Query(value = TransmissionQuery.getTransmissionsToSendMail)
    Flux<TransmissionNotiDTO> getTransmissionsToSendMail(Integer resendCount);

    @Query(value = TransmissionQuery.updateTransmissionState)
    Mono<Boolean> updateTransmissionSuccessState(List<String> transmissionIds);

    @Query(value = TransmissionQuery.updateTransmissionStateAndResendCount)
    Mono<Boolean> updateTransmissionFailedState(List<String> transmissionIds);

    @Query(value = TransmissionQuery.updateStateById)
    Mono<Boolean> updateTransmissionStateById(String id, String state);
}
