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
import com.ezbuy.settingmodel.dto.request.SearchOptionSetRequest;
import com.ezbuy.settingmodel.model.OptionSet;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.ezbuy.settingmodel.request.CreateOptionSetRequest;
import com.ezbuy.settingmodel.response.SearchOptionSetResponse;
import com.ezbuy.settingservice.service.OptionSetService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Mono<DataResponse<OptionSet>> createOptionSet(@RequestBody CreateOptionSetRequest request) {
        return optionSetService.createOptionSet(request);
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
