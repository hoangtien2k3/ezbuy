package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.model.MarketSection;
import com.ezbuy.settingmodel.request.MarketSectionSearchRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketSectionRepositoryTemplate {
    Flux<MarketSection> queryMarketSections(MarketSectionSearchRequest request);
    Mono<Long> countMarketSections(MarketSectionSearchRequest request);
}
