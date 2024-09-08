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
import com.ezbuy.settingmodel.dto.MarketSectionDTO;
import com.ezbuy.settingmodel.model.MarketSection;
import com.ezbuy.settingmodel.request.CreateMarketSectionRequest;
import com.ezbuy.settingmodel.request.MarketSectionSearchRequest;
import com.ezbuy.settingmodel.response.MarketSectionSearchResponse;
import com.ezbuy.settingservice.service.MarketSectionService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import io.hoangtien2k3.commons.utils.DataUtil;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.MarketSection.PREFIX)
public class MarketSectionController {
    private final MarketSectionService marketSectionService;

    @GetMapping()
    public Mono<ResponseEntity<DataResponse<List<MarketSection>>>> getPage(String pageCode, String serviceId) {
        pageCode = DataUtil.safeTrim(pageCode);
        serviceId = DataUtil.safeTrim(serviceId);
        return marketSectionService
                .getMarketSection(pageCode, serviceId)
                .collectList()
                .flatMap(marketSections -> Mono.just(ResponseEntity.ok(new DataResponse<>("success", marketSections))));
    }

    @GetMapping(UrlPaths.MarketSection.GET_PAGE_V2)
    public Mono<ResponseEntity<DataResponse<List<MarketSection>>>> getPageV2(String pageCode, String serviceAlias) {
        pageCode = DataUtil.safeTrim(pageCode);
        serviceAlias = DataUtil.safeTrim(serviceAlias);
        return marketSectionService
                .getMarketSectionV2(pageCode, serviceAlias)
                .collectList()
                .flatMap(marketSections -> Mono.just(ResponseEntity.ok(new DataResponse<>("success", marketSections))));
    }

    @GetMapping(UrlPaths.MarketSection.FIND_BY_ID)
    public Mono<ResponseEntity<DataResponse<MarketSection>>> findById(@PathVariable String id) {
        id = DataUtil.safeTrim(id);
        return marketSectionService
                .findById(id)
                .flatMap(section -> Mono.just(ResponseEntity.ok(new DataResponse<>("success", section))));
    }

    @GetMapping(UrlPaths.MarketSection.SEARCH_MARKET_SECTION)
    public Mono<ResponseEntity<DataResponse<MarketSectionSearchResponse>>> searchMarketSection(
            MarketSectionSearchRequest request) {
        return marketSectionService
                .searchMarketSection(request)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.MarketSection.DETAILS)
    public Mono<ResponseEntity<DataResponse<MarketSectionDTO>>> findMarketSectionById(@PathVariable String id) {
        return marketSectionService
                .findMarketSectionById(id)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @PostMapping(value = UrlPaths.MarketSection.CREATE)
    public Mono<DataResponse<MarketSection>> createMarketSection(@RequestBody CreateMarketSectionRequest request) {
        return marketSectionService.createMarketSection(request).log();
    }

    @PutMapping(value = UrlPaths.MarketSection.EDIT)
    public Mono<DataResponse<MarketSection>> editMarketSection(
            @PathVariable String id, @Valid @RequestBody CreateMarketSectionRequest request) {
        return marketSectionService.editMarketSection(id, request);
    }

    @PutMapping(value = UrlPaths.MarketSection.DELETE)
    public Mono<DataResponse<MarketSection>> deleteMarketSection(@PathVariable String id) {
        return marketSectionService.deleteMarketSection(id);
    }

    @GetMapping(UrlPaths.MarketSection.ALL_ACTIVE)
    public Mono<ResponseEntity<DataResponse<List<MarketSection>>>> getAllActiveMarketSections() {
        return marketSectionService
                .getAllActiveMarketSections()
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.MarketSection.ALL)
    public Mono<ResponseEntity<DataResponse<List<MarketSection>>>> getAllMarketSections() {
        return marketSectionService
                .getAllMarketSections()
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.MarketSection.FIND_BY_CONTENT_SECTION_ID)
    public Mono<ResponseEntity<DataResponse<MarketSection>>> findByContentSectionId(@PathVariable String id) {
        id = DataUtil.safeTrim(id);
        return marketSectionService
                .findByContentSectionId(id)
                .flatMap(section -> Mono.just(ResponseEntity.ok(new DataResponse<>("success", section))));
    }

    @GetMapping(UrlPaths.MarketSection.ALL_ACTIVE_RT)
    // lay cac ban ghi market section theo type = rich_text
    public Mono<ResponseEntity<DataResponse<List<MarketSection>>>> getAllActiveMarketSectionsRT() {
        return marketSectionService
                .getAllActiveMarketSectionsRT()
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }
}
