package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.request.SearchMarketPageSectionRequest;
import com.ezbuy.settingmodel.model.MarketPageSection;
import com.ezbuy.settingmodel.request.MarketPageSectionRequest;
import com.ezbuy.settingmodel.response.SearchMarketPageSectionResponse;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface MarketPageSectionService {
    Mono<SearchMarketPageSectionResponse> getMarketPageSection(SearchMarketPageSectionRequest request);

    Mono<DataResponse<MarketPageSection>> createMarketPageSection(MarketPageSectionRequest request);

    Mono<DataResponse<MarketPageSection>> updateMarketPageSection(String id, MarketPageSectionRequest request);

    Mono<DataResponse<MarketPageSection>> lockMarketPageSectionById(String id);

    Mono<DataResponse<MarketPageSection>> unlockMarketPageSectionById(String id);
}
