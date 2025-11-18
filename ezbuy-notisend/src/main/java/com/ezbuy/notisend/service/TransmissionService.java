package com.ezbuy.notisend.service;

import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface TransmissionService {

    Mono<DataResponse<Object>> sendNotification();
}
