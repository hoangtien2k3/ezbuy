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
import com.ezbuy.settingmodel.model.MarketPage;
import com.ezbuy.settingmodel.request.GetByServiceRequest;
import com.ezbuy.settingmodel.request.MarketPageRequest;
import com.ezbuy.settingmodel.request.SearchMarketPageRequest;
import com.ezbuy.settingmodel.request.v2.GetByServiceRequestV2;
import com.ezbuy.settingmodel.response.SearchMarketPageResponse;
import com.ezbuy.settingservice.service.MarketPageService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.MarketPage.PREFIX)
public class MarketPageController {
    private final MarketPageService marketPageService;

    @GetMapping()
    public Mono<ResponseEntity<DataResponse<SearchMarketPageResponse>>> search(SearchMarketPageRequest request) {
        return marketPageService
                .searchMarketPage(request)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.MarketPage.GET_ALL_MP)
    public Mono<ResponseEntity<DataResponse<List<MarketPage>>>> getAllMarketPage() {
        return marketPageService
                .getAllMarketPage()
                .map(result -> ResponseEntity.ok(new DataResponse<>("success", result)));
    }

    @GetMapping(UrlPaths.MarketPage.DETAILS)
    public Mono<DataResponse<MarketPage>> getInfo(@PathVariable("id") String id) {
        return marketPageService.getMarketPage(id);
    }

    @PostMapping()
    public Mono<DataResponse<DataResponse<MarketPage>>> createMarketPage(
            @Valid @RequestBody MarketPageRequest request) {
        return marketPageService.createMarketPage(request).map(result -> new DataResponse<>("success", result));
    }

    @PutMapping()
    public Mono<DataResponse<MarketPage>> updateMarketPage(@Valid @RequestBody MarketPageRequest request) {
        return marketPageService.updateMarketPage(request);
    }

    @GetMapping(UrlPaths.MarketPage.ALL_ACTIVE)
    public Mono<DataResponse<List<MarketPage>>> getAllActiveMarketPage() {
        return marketPageService.getAllActiveMarketPage().map(result -> new DataResponse<>("success", result));
    }

    @PostMapping(UrlPaths.MarketPage.LIST_BY_SERVICE)
    public Mono<DataResponse<List<MarketPage>>> getMarketPageByServiceId(@RequestBody GetByServiceRequest request) {
        return marketPageService.getMarketPageByServiceId(request.getLstServiceId());
    }

    @PostMapping(UrlPaths.MarketPage.LIST_BY_SERVICE_V2)
    public Mono<DataResponse<List<MarketPage>>> getMarketPageByServiceIdV2(@RequestBody GetByServiceRequestV2 request) {
        return marketPageService.getMarketPageByServiceIdV2(request.getLstAlias());
    }
}
