package com.ezbuy.settingservice.controller;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.model.MarketInfo;
import com.ezbuy.settingmodel.request.GetByServiceRequest;
import com.ezbuy.settingmodel.request.MarketInfoRequest;
import com.ezbuy.settingmodel.request.SearchMarketInfoRequest;
import com.ezbuy.settingmodel.request.v2.GetByServiceRequestV2;
import com.ezbuy.settingmodel.response.SearchMarketInfoResponse;
import com.ezbuy.settingservice.service.MarketInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.MarketInfo.PREFIX)
public class MarketInfoController {
    private final MarketInfoService marketInfoService;

//    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping(UrlPaths.MarketInfo.SEARCH)
    public Mono<DataResponse<SearchMarketInfoResponse>> search(SearchMarketInfoRequest request) {
        return marketInfoService.searchMarketInfo(request);
    }

//    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping(UrlPaths.MarketInfo.LIST)
    public Mono<DataResponse<List<MarketInfo>>> getAllMarketInfo() {
        return marketInfoService.getAllMarketInfo();
    }

//    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping(UrlPaths.MarketInfo.DETAILS)
    public Mono<DataResponse<MarketInfo>> getInfo(@PathVariable("id") String id) {
        return marketInfoService.getMarketInfo(id);
    }

//    @PreAuthorize("hasAnyAuthority('user')")
    @PostMapping(UrlPaths.MarketInfo.CREATE)
    public Mono<DataResponse> createMarketInfo(@Valid @RequestBody MarketInfoRequest request) {
        return marketInfoService.createMarketInfo(request)
                .map(result -> new DataResponse<>("success", result));
    }

//    @PreAuthorize("hasAnyAuthority('user')")
    @PutMapping(UrlPaths.MarketInfo.EDIT)
    public Mono<DataResponse<MarketInfo>> updateMarketInfo(@PathVariable String id, @Valid @RequestBody MarketInfoRequest request) {
        return marketInfoService.updateMarketInfo(id, request);
    }

    @GetMapping(UrlPaths.MarketInfo.ALL_ACTIVE)
    public Mono<DataResponse<List<MarketInfo>>> getAllActiveMarketInfo() {
        return marketInfoService.getAllActiveMarketInfo()
                .map(result -> new DataResponse<>("success", result));
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
