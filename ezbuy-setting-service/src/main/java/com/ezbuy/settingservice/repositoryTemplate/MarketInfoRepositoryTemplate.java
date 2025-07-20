package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.MarketInfoDTO;
import com.ezbuy.settingmodel.request.SearchMarketInfoRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MarketInfoRepositoryTemplate {
    Flux<MarketInfoDTO> queryList(SearchMarketInfoRequest request);

    Mono<Long> count(SearchMarketInfoRequest request);
}
