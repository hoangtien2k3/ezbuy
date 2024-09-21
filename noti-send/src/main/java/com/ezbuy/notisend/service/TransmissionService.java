package com.ezbuy.notisend.service;

import io.hoangtien2k3.commons.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface TransmissionService {
    Mono<DataResponse<Object>> sendNotification();
}
