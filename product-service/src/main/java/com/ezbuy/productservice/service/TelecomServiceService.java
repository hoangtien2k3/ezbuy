package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.TelecomServiceResponse;
import reactor.core.publisher.Flux;

public interface TelecomServiceService {

    Flux<TelecomServiceResponse> getTelecomServices();
}
