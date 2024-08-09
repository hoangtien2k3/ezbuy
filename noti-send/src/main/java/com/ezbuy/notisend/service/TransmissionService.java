package com.ezbuy.notisend.service;

import com.ezbuy.framework.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface TransmissionService {
    Mono<DataResponse<Object>> sendNotification();
}
