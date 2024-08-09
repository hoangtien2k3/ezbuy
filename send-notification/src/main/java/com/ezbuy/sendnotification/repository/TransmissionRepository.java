package com.ezbuy.sendnotification.repository;

import com.ezbuy.sendnotification.repository.query.TransmissionQuery;
import com.ezbuy.sendnotification.model.Transmission;
import com.ezbuy.sendnotification.model.noti.TransmissionNotiDTO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransmissionRepository extends R2dbcRepository<Transmission, String> {
    @Query(value = TransmissionQuery.getTransmissionsToSendMail)
    Flux<TransmissionNotiDTO> getTransmissionsToSendMail(Integer resendCount, Integer limit);

    @Query(value = TransmissionQuery.updateTransmissionRestState)
    Mono<Boolean> updateTransmissionRestSuccessState(List<String> transmissionIds);

    @Query(value = TransmissionQuery.updateTransmissionEmailState)
    Mono<Boolean> updateTransmissionEmailSuccessState(List<String> transmissionIds);

    @Query(value = TransmissionQuery.updateTransmissionStateAndResendCount)
    Mono<Boolean> updateTransmissionFailedState(List<String> transmissionIds);

    @Query(value = TransmissionQuery.updateStateById)
    Mono<Boolean> updateTransmissionStateById(String id, String state);

    @Query(value = TransmissionQuery.findByIdAndStatus)
    Mono<Transmission> findByIdAndStatus(String id, int status);

    @Query(value = TransmissionQuery.getTransmissionByNotificationContentId)
    Flux<String> getListTransId(String receiver, String notificationContentId);

    @Query(value = TransmissionQuery.changeStateTransmissionByType)
    Mono<Void> changeStateTransmissionByNotiIdAndReceiver(String state, String receiver, String notificationContentId);
}
