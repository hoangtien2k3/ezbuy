package com.ezbuy.settingservice.controller;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.model.Setting;
import com.ezbuy.settingmodel.request.CreateSettingRequest;
import com.ezbuy.settingmodel.request.SearchSettingRequest;
import com.ezbuy.settingmodel.response.SearchSettingResponse;
import com.ezbuy.settingservice.service.SettingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.Setting.PREFIX)
public class SettingController {
    private final SettingService settingService;

    @GetMapping(value = UrlPaths.Setting.FIND_BY_CODE)
    public Mono<DataResponse<String>> findByCode(@PathVariable String code) {
        return settingService.findByCode(code).map(rs -> new DataResponse<>("success", rs));
    }

    @GetMapping(value = UrlPaths.Setting.SEARCH)
    public Mono<DataResponse<SearchSettingResponse>> search(SearchSettingRequest request) {
        return settingService.searchSetting(request).map(rs -> new DataResponse<>("success", rs));
    }

    @GetMapping(UrlPaths.Setting.GET_ALL)
    public Mono<DataResponse<List<Setting>>> getAllSetting() {
        return settingService.getAllSetting()
                .map(result -> new DataResponse<>("success", result));
    }

    @GetMapping(UrlPaths.Setting.ALL_ACTIVE)
    public Mono<DataResponse<List<Setting>>> getAllActiveSetting() {
        return settingService.getAllActiveSetting()
                .map(result -> new DataResponse<>("success", result));
    }

    @PostMapping(UrlPaths.Setting.CREATE)
    public Mono<DataResponse<Setting>> createSetting(@RequestBody CreateSettingRequest request) {
        return settingService.createSetting(request);
    }

    @PutMapping(value = UrlPaths.Setting.EDIT)
    public Mono<DataResponse<Setting>> updateSetting(@PathVariable String id, @Valid @RequestBody CreateSettingRequest request) {
        return settingService.updateSetting(id, request);
    }

    @PutMapping(value = UrlPaths.Setting.DELETE)
    public Mono<DataResponse<Setting>> deleteSetting(@PathVariable String id) {
        return settingService.deleteSetting(id);
    }
}
