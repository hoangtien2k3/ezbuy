package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.OptionSetValueDTO;
import com.ezbuy.settingmodel.dto.request.SearchOptionSetValueRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OptionSetValueRepositoryTemplate {
    Flux<OptionSetValueDTO> findOptionSetValue(SearchOptionSetValueRequest request);
    Mono<Long> countOptionSetValue(SearchOptionSetValueRequest request);
}
