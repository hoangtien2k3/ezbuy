package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.model.Telecom;
import java.util.List;

import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface TelecomService {
    Mono<DataResponse<Telecom>> getByOriginId(String originId);

    Mono<DataResponse<List<Telecom>>> getAllActive();

    Mono<DataResponse<Telecom>> getByAlias(String telecomServiceAlias);

    /**
     * Lay danh sach cau hinh lst dich vu theo alias hub
     *
     * @param lstTelecomServiceAlias
     * @return
     */
    Mono<DataResponse<List<Telecom>>> getByLstAlias(List<String> lstTelecomServiceAlias);
}
