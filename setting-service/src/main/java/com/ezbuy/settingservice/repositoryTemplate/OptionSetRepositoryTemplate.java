package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.OptionSetDTO;
import com.ezbuy.settingmodel.dto.request.SearchOptionSetRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OptionSetRepositoryTemplate {
    Flux<OptionSetDTO> findOptionSet(SearchOptionSetRequest request);
    Mono<Long> countOptionSet(SearchOptionSetRequest request);
}
