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
package com.ezbuy.settingservice.controller;

import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.model.MarketInfo;
import com.ezbuy.settingmodel.request.GetByServiceRequest;
import com.ezbuy.settingmodel.request.MarketInfoRequest;
import com.ezbuy.settingmodel.request.SearchMarketInfoRequest;
import com.ezbuy.settingmodel.request.v2.GetByServiceRequestV2;
import com.ezbuy.settingmodel.response.SearchMarketInfoResponse;
import com.ezbuy.settingservice.service.MarketInfoService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.MarketInfo.PREFIX)
public class MarketInfoController {
    private final MarketInfoService marketInfoService;

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping(UrlPaths.MarketInfo.SEARCH)
    public Mono<DataResponse<SearchMarketInfoResponse>> search(SearchMarketInfoRequest request) {
        return marketInfoService.searchMarketInfo(request);
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping(UrlPaths.MarketInfo.LIST)
    public Mono<DataResponse<List<MarketInfo>>> getAllMarketInfo() {
        return marketInfoService.getAllMarketInfo();
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping(UrlPaths.MarketInfo.DETAILS)
    public Mono<DataResponse<MarketInfo>> getInfo(@PathVariable("id") String id) {
        return marketInfoService.getMarketInfo(id);
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @PostMapping(UrlPaths.MarketInfo.CREATE)
    public Mono<DataResponse> createMarketInfo(@Valid @RequestBody MarketInfoRequest request) {
        return marketInfoService.createMarketInfo(request).map(result -> new DataResponse<>("success", result));
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @PutMapping(UrlPaths.MarketInfo.EDIT)
    public Mono<DataResponse<MarketInfo>> updateMarketInfo(
            @PathVariable String id, @Valid @RequestBody MarketInfoRequest request) {
        return marketInfoService.updateMarketInfo(id, request);
    }

    @GetMapping(UrlPaths.MarketInfo.ALL_ACTIVE)
    public Mono<DataResponse<List<MarketInfo>>> getAllActiveMarketInfo() {
        return marketInfoService.getAllActiveMarketInfo().map(result -> new DataResponse<>("success", result));
    }

    @PostMapping(UrlPaths.MarketInfo.LIST_BY_SERVICE)
    public Mono<DataResponse<List<MarketInfo>>> getMarketInfoByServiceId(@RequestBody GetByServiceRequest request) {
        return marketInfoService.getMarketInfoByServiceId(request.getLstServiceId());
    }

    @PostMapping(UrlPaths.MarketInfo.LIST_BY_SERVICE_V2)
    public Mono<DataResponse<List<MarketInfo>>> getMarketInfoByServiceIdV2(@RequestBody GetByServiceRequestV2 request) {
        return marketInfoService.getMarketInfoByServiceIdV2(request.getLstAlias());
    }
}
