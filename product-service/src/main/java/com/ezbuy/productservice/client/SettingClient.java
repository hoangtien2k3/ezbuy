package com.ezbuy.productservice.client;

import com.ezbuy.settingmodel.dto.TelecomDTO;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface SettingClient {
    Mono<List<TelecomDTO>> getTelecomService(String aliases);

    Mono<List<TelecomDTO>> getTelecomServiceByOriginId(String originIds);

    <T> Mono<DataResponse<T>> updateIsFilter(String telecomServiceId);

    Mono<List<TelecomDTO>> getTelecomServices(List<String> originalIds);
}
