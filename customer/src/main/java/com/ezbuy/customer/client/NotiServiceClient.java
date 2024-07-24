package com.ezbuy.customer.client;

import java.util.Optional;

import com.ezbuy.customer.model.dto.request.CreateNotificationDTO;
import com.ezbuy.framework.model.response.DataResponse;

import reactor.core.publisher.Mono;

public interface NotiServiceClient {

    Mono<Optional<DataResponse>> insertTransmission(CreateNotificationDTO createNotificationDTO);
}
