package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.request.SearchMarketPageSectionRequest;
import com.ezbuy.settingmodel.model.MarketPageSection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketPageSectionRepositoryTemplate {
    Flux<MarketPageSection> findMarketPageSection(SearchMarketPageSectionRequest request);

    Mono<Long> countMarketPageSection(SearchMarketPageSectionRequest request);
}
