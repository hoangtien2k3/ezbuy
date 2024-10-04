package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.model.MarketPage;
import com.ezbuy.settingmodel.request.MarketPageRequest;
import com.ezbuy.settingmodel.request.SearchMarketPageRequest;
import com.ezbuy.settingmodel.response.SearchMarketPageResponse;
import io.hoangtien2k3.reactify.model.response.DataResponse;
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
