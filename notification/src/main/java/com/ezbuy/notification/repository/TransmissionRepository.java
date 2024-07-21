package com.ezbuy.notification.repository;

import com.ezbuy.notification.dto.TransmissionNotiDTO;
import com.ezbuy.notification.dto.response.CountNoticeDTO;
import com.ezbuy.notification.model.NotificationContent;
import com.ezbuy.notification.model.Transmission;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static com.ezbuy.notification.repository.query.TransmissionQuery.*;

public interface TransmissionRepository extends R2dbcRepository<Transmission, String> {
    @Query(value = getCountNoticeDTO)
    Flux<CountNoticeDTO> getListCountNoticeDTO(String receiver);

    @Query(value = getTransmissionByNotificationContentId)
    Flux<String> getListTransId(String receiver, String notificationContentId);

    @Query(value = changeStateTransmissionByType)
    Mono<Void> changeStateTransmissionByNotiIdAndReceiver(String state, String receiver, String notificationContentId);

    @Query(value = getAllNotificationContentByCreateAtAfter)
    Flux<NotificationContent> getAllNotificationContentByCreateAtAfter(String receiver, LocalDateTime newestNotiTime);

    @Query(value = findByIdAndStatus)
    Mono<Transmission> findByIdAndStatus(String id, int status);

    @Query(value = getTransmissionsToSendMail)
    Flux<TransmissionNotiDTO> getTransmissionsToSendMail(Integer resendCount);

    @Query(value = updateTransmissionState)
    Mono<Boolean> updateTransmissionSuccessState(List<String> transmissionIds);

    @Query(value = updateTransmissionStateAndResendCount)
    Mono<Boolean> updateTransmissionFailedState(List<String> transmissionIds);

    @Query(value = updateStateById)
    Mono<Boolean> updateTransmissionStateById(String id, String state);
}
