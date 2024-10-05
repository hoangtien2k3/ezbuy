package com.ezbuy.productservice.client;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.settingmodel.dto.TelecomDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SettingClient {
    Mono<List<TelecomDTO>> getTelecomService(String aliases);
    Mono<List<TelecomDTO>> getTelecomServiceByOriginId(String originIds);

    <T> Mono<DataResponse<T>> updateIsFilter(String telecomServiceId);
    Mono<List<TelecomDTO>> getTelecomServices(List<String> originalIds);
}
