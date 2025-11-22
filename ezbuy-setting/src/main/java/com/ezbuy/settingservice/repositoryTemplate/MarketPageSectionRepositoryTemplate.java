package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.request.SearchMarketPageSectionRequest;
import com.ezbuy.settingservice.model.entity.MarketPageSection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketPageSectionRepositoryTemplate {
    Flux<MarketPageSection> findMarketPageSection(SearchMarketPageSectionRequest request);

    Mono<Long> countMarketPageSection(SearchMarketPageSectionRequest request);
}
