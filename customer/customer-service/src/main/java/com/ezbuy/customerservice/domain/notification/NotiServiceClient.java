package com.ezbuy.customerservice.domain.notification;

import com.ezbuy.customermodel.dto.request.CreateNotificationDTO;
import com.ezbuy.framework.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface NotiServiceClient {
    Mono<Optional<DataResponse>> insertTransmission(CreateNotificationDTO createNotificationDTO);
}
