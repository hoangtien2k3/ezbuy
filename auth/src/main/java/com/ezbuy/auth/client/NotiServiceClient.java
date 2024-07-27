package com.ezbuy.auth.client;

import java.util.Optional;

import com.ezbuy.auth.dto.request.CreateNotificationDTO;
import com.ezbuy.framework.model.response.DataResponse;

import reactor.core.publisher.Mono;

public interface NotiServiceClient {

    Mono<Optional<DataResponse>> insertTransmission(CreateNotificationDTO createNotificationDTO);
}
