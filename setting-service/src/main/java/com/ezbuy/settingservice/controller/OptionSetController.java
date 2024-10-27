package com.ezbuy.settingservice.controller;

import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.dto.request.SearchOptionSetRequest;
import com.ezbuy.settingmodel.model.OptionSet;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.ezbuy.settingmodel.request.CreateOptionSetRequest;
import com.ezbuy.settingmodel.response.SearchOptionSetResponse;
import com.ezbuy.settingservice.service.OptionSetService;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.OptionSet.PREFIX)
public class OptionSetController {
    private final OptionSetService optionSetService;

    @GetMapping(value = UrlPaths.OptionSet.FIND_BY_OPTION_SET_CODE)
    public Mono<DataResponse<List<OptionSetValue>>> findByOptionSetCode(@RequestParam String optionSetCode) {
        return optionSetService.findByOptionSetCode(optionSetCode).map(rs -> new DataResponse<>("success", rs));
    }

    @GetMapping(value = UrlPaths.OptionSet.FIND_BY_CODE)
    public Mono<DataResponse<OptionSetValue>> findByCode(
            @RequestParam String optionSetCode, @RequestParam String optionSetValueCode) {
        return optionSetService
                .findByOptionSetCodeAndOptionValueCode(optionSetCode, optionSetValueCode)
                .map(rs -> new DataResponse<>("success", rs));
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse<OptionSet>>> createOptionSet(@RequestBody CreateOptionSetRequest request) {
        return optionSetService.createOptionSet(request)
                .map(ResponseEntity::ok);
    }

    @PutMapping(value = UrlPaths.OptionSet.UPDATE)
    public Mono<DataResponse<OptionSet>> editOptionSet(
            @PathVariable String id, @Valid @RequestBody CreateOptionSetRequest request) {
        return optionSetService.editOptionSet(id, request);
    }

    @GetMapping(value = UrlPaths.OptionSet.ALL)
    public Mono<SearchOptionSetResponse> findOptionSet(@ModelAttribute SearchOptionSetRequest request) {
        return optionSetService.findOptionSet(request);
    }
}
