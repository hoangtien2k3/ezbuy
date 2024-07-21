package com.ezbuy.customer.client;

import com.ezbuy.customer.model.dto.request.CreateNotificationDTO;
import com.ezbuy.framework.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface NotiServiceClient {

    Mono<Optional<DataResponse>> insertTransmission(CreateNotificationDTO createNotificationDTO);

}
