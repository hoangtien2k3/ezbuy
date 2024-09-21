package com.ezbuy.settingservice.controller;

import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.dto.request.GlobalSearchSyncRequest;
import com.ezbuy.settingservice.service.GlobalSearchService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.GlobalSearch.PREFIX)
public class GlobalSearchSyncController {

    private final GlobalSearchService globalSearchService;

    @PostMapping(UrlPaths.GlobalSearch.SERVICES)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<DataResponse> syncService(@RequestBody GlobalSearchSyncRequest request) {
        return globalSearchService.syncService(request);
    }

    @PostMapping(UrlPaths.GlobalSearch.NEWS)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<DataResponse> syncNews(@RequestBody GlobalSearchSyncRequest request) {
        return globalSearchService.syncNews(request);
    }
}
