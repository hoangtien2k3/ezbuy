package com.ezbuy.settingservice.service;

import com.ezbuy.settingservice.model.entity.MarketPage;
import com.ezbuy.settingservice.model.dto.request.MarketPageRequest;
import com.ezbuy.settingservice.model.dto.request.SearchMarketPageRequest;
import com.ezbuy.settingservice.model.dto.response.SearchMarketPageResponse;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface MarketPageService {
    Mono<SearchMarketPageResponse> searchMarketPage(SearchMarketPageRequest request);

    Mono<List<MarketPage>> getAllMarketPage();

    Mono<DataResponse<MarketPage>> getMarketPage(String id);

    Mono<DataResponse<MarketPage>> createMarketPage(MarketPageRequest request);

    Mono<DataResponse<MarketPage>> updateMarketPage(MarketPageRequest request);

    Mono<List<MarketPage>> getAllActiveMarketPage();

    Mono<DataResponse<List<MarketPage>>> getMarketPageByServiceId(List<String> lstServiceId);

    Mono<DataResponse<List<MarketPage>>> getMarketPageByServiceIdV2(List<String> lstAlias);
}
