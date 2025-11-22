package com.ezbuy.settingservice.controller;

import com.ezbuy.settingservice.constants.UrlPaths;
import com.ezbuy.settingservice.model.dto.request.SearchMarketPageSectionRequest;
import com.ezbuy.settingservice.model.entity.MarketPageSection;
import com.ezbuy.settingservice.model.dto.request.MarketPageSectionRequest;
import com.ezbuy.settingservice.model.dto.response.SearchMarketPageSectionResponse;
import com.ezbuy.settingservice.service.MarketPageSectionService;
import com.ezbuy.core.model.response.DataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.MarketPageSection.PREFIX)
public class MarketPageSectionController {
    private final MarketPageSectionService marketPageSectionService;

    @GetMapping(value = UrlPaths.MarketPageSection.DETAIL)
    public Mono<SearchMarketPageSectionResponse> getMarketPageSection(SearchMarketPageSectionRequest request) {
        return marketPageSectionService.getMarketPageSection(request);
    }

    @PostMapping(UrlPaths.MarketPageSection.CREATE)
    public Mono<DataResponse<MarketPageSection>> createMarketPageSection(
            @Valid @RequestBody MarketPageSectionRequest request) {
        return marketPageSectionService.createMarketPageSection(request);
    }

    @PutMapping(UrlPaths.MarketPageSection.EDIT)
    public Mono<DataResponse<MarketPageSection>> editMarketPageSection(
            @PathVariable String id, @Valid @RequestBody MarketPageSectionRequest request) {
        return marketPageSectionService.updateMarketPageSection(id, request);
    }
}
