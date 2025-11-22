package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.entity.MarketSection;
import com.ezbuy.settingservice.model.dto.request.MarketSectionSearchRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketSectionRepositoryTemplate {
    Flux<MarketSection> queryMarketSections(MarketSectionSearchRequest request);

    Mono<Long> countMarketSections(MarketSectionSearchRequest request);
}
