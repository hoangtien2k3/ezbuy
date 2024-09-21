package com.ezbuy.customer.client;

import com.ezbuy.customer.model.dto.request.CreateNotificationDTO;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface NotiServiceClient {

    Mono<Optional<DataResponse>> insertTransmission(CreateNotificationDTO createNotificationDTO);
}
