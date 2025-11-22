package com.ezbuy.settingservice.service;

import com.ezbuy.settingservice.model.dto.request.SearchMarketPageSectionRequest;
import com.ezbuy.settingservice.model.entity.MarketPageSection;
import com.ezbuy.settingservice.model.dto.request.MarketPageSectionRequest;
import com.ezbuy.settingservice.model.dto.response.SearchMarketPageSectionResponse;
import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface MarketPageSectionService {
    Mono<SearchMarketPageSectionResponse> getMarketPageSection(SearchMarketPageSectionRequest request);

    Mono<DataResponse<MarketPageSection>> createMarketPageSection(MarketPageSectionRequest request);

    Mono<DataResponse<MarketPageSection>> updateMarketPageSection(String id, MarketPageSectionRequest request);
}
