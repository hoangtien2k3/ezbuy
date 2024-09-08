/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.model.MarketInfo;
import com.ezbuy.settingmodel.request.MarketInfoRequest;
import com.ezbuy.settingmodel.request.SearchMarketInfoRequest;
import com.ezbuy.settingmodel.response.SearchMarketInfoResponse;
import io.hoangtien2k3.commons.model.response.DataResponse;
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
