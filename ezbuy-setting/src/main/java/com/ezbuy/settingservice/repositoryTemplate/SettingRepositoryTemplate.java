package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.SettingDTO;
import com.ezbuy.settingservice.model.dto.request.SearchSettingRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SettingRepositoryTemplate {

    Flux<SettingDTO> searchSettingByRequest(SearchSettingRequest request);

    Mono<Long> count(SearchSettingRequest request);
}
