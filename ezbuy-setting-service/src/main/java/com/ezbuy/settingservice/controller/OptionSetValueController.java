package com.ezbuy.settingservice.controller;

import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.dto.OptionSetValueDTO;
import com.ezbuy.settingmodel.dto.request.SearchOptionSetValueRequest;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.ezbuy.settingmodel.request.CreateOptionSetValueRequest;
import com.ezbuy.settingmodel.response.SearchOptionSetValueResponse;
import com.ezbuy.settingservice.service.OptionSetValueService;
import com.reactify.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.OptionSetValue.PREFIX)
public class OptionSetValueController {
    private final OptionSetValueService optionSetValueService;

    @GetMapping(UrlPaths.OptionSetValue.GET_OPTION_SET_VALUE_BY_OPTION_SET_CODE)
    public Mono<DataResponse<List<OptionSetValueDTO>>> getAllActiveDataPolicyConfigByOptionSetCode(String code) {
        return optionSetValueService
                .getAllActiveDataPolicyConfigByOptionSetCode(code)
                .map(result -> new DataResponse<>("success", result));
    }

    @GetMapping(UrlPaths.OptionSetValue.GET_OPTION_SET_VALUE_BY_CODE)
    public Mono<DataResponse<List<OptionSetValue>>> getAllActiveOptionSetValueByOptionSetCode(String code) {
        return optionSetValueService
                .getAllActiveOptionSetValueByOptionSetCode(code)
                .map(result -> new DataResponse<>("success", result));
    }

    @PostMapping
    public Mono<DataResponse<OptionSetValue>> createOptionSetValue(@RequestBody CreateOptionSetValueRequest request) {
        return optionSetValueService.createOptionSetValue(request);
    }

    @PutMapping(value = UrlPaths.OptionSetValue.UPDATE)
    public Mono<DataResponse<OptionSetValue>> editOptionSetValue(
            @PathVariable String id, @Valid @RequestBody CreateOptionSetValueRequest request) {
        return optionSetValueService.editOptionSetValue(id, request);
    }

    @GetMapping
    public Mono<SearchOptionSetValueResponse> findOptionSetValueByOptionSetId(
            @ModelAttribute SearchOptionSetValueRequest request) {
        return optionSetValueService.findOptionSetValueByOptionSetId(request);
    }

    @GetMapping(UrlPaths.OptionSetValue.GET_ACROYNM_BY_LIST_ALISAS)
    public Mono<DataResponse<List<OptionSetValue>>> getLstAcronymByAliases(
            @RequestParam(value = "code") String code,
            @RequestParam(value = "serviceAliases") List<String> serviceAliases) {
        return optionSetValueService
                .getLstAcronymByAliases(code, serviceAliases)
                .map(result -> new DataResponse<>("success", result));
    }
}
