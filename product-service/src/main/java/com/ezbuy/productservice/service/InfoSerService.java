package com.ezbuy.productservice.service;

import com.ezbuy.settingmodel.dto.TelecomDTO;
import java.util.List;
import reactor.core.publisher.Mono;

public interface InfoSerService {

    Mono<List<TelecomDTO>> getActiveTelecomService(List<String> idNo);

    Mono<List<String>> getActiveTelecomServiceIdByOrgId(String idNo);

    Mono<List<String>> getActiveTelecomServiceAlilasByIdNo(String idNo);
}
