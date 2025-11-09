package com.ezbuy.auth.infrastructure.client;

import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import com.ezbuy.core.model.response.DataResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface NotiServiceClient {

    /**
     * Inserts a new transmission.
     *
     * @param createNotificationDTO
     *            the DTO containing the details of the notification to create
     * @return a Mono emitting an Optional containing a DataResponse if the
     *         operation was successful, or an empty Optional if not
     */
    Mono<Optional<DataResponse>> insertTransmission(CreateNotificationDTO createNotificationDTO);
}
