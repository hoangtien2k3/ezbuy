package com.ezbuy.notificationsend.service;

import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface TransmissionSendSmsService {
    Mono<DataResponse<Object>> sendSmsNotification();


}
