package com.ezbuy.productservice.service;

import com.ezbuy.sme.settingmodel.dto.TelecomDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface InfoSerService {

    Mono<List<TelecomDTO>> getActiveTelecomService(List<String> idNo);

    Mono<List<String>> getActiveTelecomServiceIdByOrgId(String idNo);

    Mono<List<String>> getActiveTelecomServiceAlilasByIdNo(String idNo);

}
