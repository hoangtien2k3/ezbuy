package com.ezbuy.customer.client;

import com.ezbuy.customer.model.dto.request.CreateNotificationDTO;
import com.ezbuy.reactify.model.response.DataResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface NotiServiceClient {

    Mono<Optional<DataResponse>> insertTransmission(CreateNotificationDTO createNotificationDTO);
}
