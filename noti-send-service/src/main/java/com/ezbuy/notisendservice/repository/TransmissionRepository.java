package com.ezbuy.notisendservice.repository;

import com.ezbuy.notimodel.dto.TransmissionNotiDTO;
import com.ezbuy.notimodel.model.Transmission;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.ezbuy.notisendservice.repository.query.TransmissionQuery.*;

public interface TransmissionRepository extends R2dbcRepository<Transmission, String> {

    @Query(value = GET_TRANSMISSIONS_TO_SEND_MAIL)
    Flux<TransmissionNotiDTO> getTransmissionsToSendMail(Integer resendCount, Integer limit);

    @Query(value = UPDATE_TRANSMISSION_REST_STATE)
    Mono<Boolean> updateTransmissionRestSuccessState(List<String> transmissionIds);

    @Query(value = UPDATE_TRANSMISSION_EMAIL_STATE)
    Mono<Boolean> updateTransmissionEmailSuccessState(List<String> transmissionIds);

    @Query(value = UPDATE_TRANSMISSION_STATE_AND_RESEND_COUNT)
    Mono<Boolean> updateTransmissionFailedState(List<String> transmissionIds);

    @Query(value = UPDATE_STATE_BY_ID)
    Mono<Boolean> updateTransmissionStateById(String id, String state);

    @Query(value = FIND_BY_ID_AND_STATUS)
    Mono<Transmission> findByIdAndStatus(String id, int status);

    @Query(value = GET_TRANSMISSION_BY_NOTIFICATION_CONTENT_ID)
    Flux<String> getListTransId(String receiver, String notificationContentId);

    @Query(value = CHANGE_STATE_TRANSMISSION_BY_TYPE)
    Mono<Void> changeStateTransmissionByNotiIdAndReceiver(String state, String receiver, String notificationContentId);
}
