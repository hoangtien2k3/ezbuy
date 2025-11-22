package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.MarketInfoDTO;
import com.ezbuy.settingservice.model.dto.request.SearchMarketInfoRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketInfoRepositoryTemplate {
    Flux<MarketInfoDTO> queryList(SearchMarketInfoRequest request);

    Mono<Long> count(SearchMarketInfoRequest request);
}
