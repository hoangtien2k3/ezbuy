package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.model.MarketInfo;
import com.ezbuy.settingmodel.request.MarketInfoRequest;
import com.ezbuy.settingmodel.request.SearchMarketInfoRequest;
import com.ezbuy.settingmodel.response.SearchMarketInfoResponse;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface MarketInfoService {
    Mono<DataResponse<SearchMarketInfoResponse>> searchMarketInfo(SearchMarketInfoRequest request);

    Mono<DataResponse<List<MarketInfo>>> getAllMarketInfo();

    Mono<DataResponse<MarketInfo>> getMarketInfo(String id);

    Mono<DataResponse> createMarketInfo(MarketInfoRequest request);

    Mono<DataResponse<MarketInfo>> updateMarketInfo(String id, MarketInfoRequest request);

    Mono<List<MarketInfo>> getAllActiveMarketInfo();

    Mono<DataResponse<List<MarketInfo>>> getMarketInfoByServiceId(List<String> lstServiceId);

    Mono<DataResponse<List<MarketInfo>>> getMarketInfoByServiceIdV2(List<String> lstAlias);
}
