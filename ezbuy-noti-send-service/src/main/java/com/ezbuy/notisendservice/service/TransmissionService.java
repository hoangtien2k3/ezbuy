package com.ezbuy.notisendservice.service;

import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface TransmissionService {

    Mono<DataResponse<Object>> sendNotification();
}
