package com.ezbuy.settingservice.controller;

import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.dto.request.SearchMarketPageSectionRequest;
import com.ezbuy.settingmodel.model.MarketPageSection;
import com.ezbuy.settingmodel.request.MarketPageSectionRequest;
import com.ezbuy.settingmodel.response.SearchMarketPageSectionResponse;
import com.ezbuy.settingservice.service.MarketPageSectionService;
import com.reactify.model.response.DataResponse;
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
    public Mono<DataResponse<MarketPageSection>> createMarketPageSection (@Valid @RequestBody MarketPageSectionRequest request) {
        return marketPageSectionService.createMarketPageSection(request);
    }

    @PutMapping(UrlPaths.MarketPageSection.EDIT)
    public Mono<DataResponse<MarketPageSection>> editMarketPageSection(@PathVariable String id, @Valid @RequestBody MarketPageSectionRequest request) {
        return marketPageSectionService.updateMarketPageSection(id, request);
    }

    @PutMapping(value = UrlPaths.MarketPageSection.LOCK)
    public Mono<DataResponse<MarketPageSection>> lockMarketSection(@PathVariable String id) {
        return marketPageSectionService.lockMarketPageSectionById(id);
    }

    @PutMapping(value = UrlPaths.MarketPageSection.UNLOCK)
    public Mono<DataResponse<MarketPageSection>> unlockMarketSection(@PathVariable String id) {
        return marketPageSectionService.unlockMarketPageSectionById(id);
    }
}
