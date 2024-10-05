package com.ezbuy.productservice.service;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.settingmodel.model.Telecom;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TelecomService {
    Mono<DataResponse<Telecom>> getByOriginId(String originId);
    Mono<DataResponse<List<Telecom>>> getAllActive();
    Mono<DataResponse<Telecom>> getByAlias(String telecomServiceAlias);

    /**
     * Lay danh sach cau hinh lst dich vu theo alias hub
     * @param lstTelecomServiceAlias
     * @return
     */
    Mono<DataResponse<List<Telecom>>> getByLstAlias(List<String> lstTelecomServiceAlias);
}
