package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.MarketPageDTO;
import com.ezbuy.settingmodel.request.SearchMarketPageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketPageRepositoryTemplate {
    Flux<MarketPageDTO> queryList(SearchMarketPageRequest request);

    Mono<Long> count(SearchMarketPageRequest request);
}
