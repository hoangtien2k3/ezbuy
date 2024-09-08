/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.notisend.repository;

import com.ezbuy.notimodel.dto.TransmissionNotiDTO;
import com.ezbuy.notimodel.model.Transmission;
import com.ezbuy.notisend.repository.query.TransmissionQuery;
import java.util.List;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
