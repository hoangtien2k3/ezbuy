package com.ezbuy.authservice.client;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Client interface for interacting with the Notification Service.
 */
public interface NotiServiceClient {

    /**
     * Inserts a new transmission.
     *
     * @param createNotificationDTO the DTO containing the details of the notification to create
     * @return a Mono emitting an Optional containing a DataResponse if the operation was successful, or an empty Optional if not
     */
    Mono<Optional<DataResponse>> insertTransmission(CreateNotificationDTO createNotificationDTO);
}