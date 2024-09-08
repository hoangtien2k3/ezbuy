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
import com.ezbuy.settingmodel.dto.ContentDisplayDTO;
import com.ezbuy.settingmodel.request.ComponentPageRequest;
import com.ezbuy.settingmodel.response.SearchingComponentResponse;
import com.ezbuy.settingservice.service.ContentDisplayService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import io.hoangtien2k3.commons.utils.DataUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = UrlPaths.ContentDisplay.PREFIX)
public class DisplayContentController {

    private final ContentDisplayService contentDisplayService;

    @GetMapping()
    public Mono<ResponseEntity<DataResponse<SearchingComponentResponse>>> searchComponents(
            @ModelAttribute ComponentPageRequest request) {
        return this.contentDisplayService
                .searchOriginComponents(request)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.ContentDisplay.ORIGIN_DETAILS)
    public Mono<ResponseEntity<DataResponse<List<ContentDisplayDTO>>>> getOriginWithDetails(
            @RequestParam(value = "name", required = false) String name) {
        return this.contentDisplayService
                .getOriginComponentDetails(name)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.ContentDisplay.DETAILS)
    public Mono<ResponseEntity<DataResponse<ContentDisplayDTO>>> getDetails(@PathVariable(name = "id") String id) {
        return this.contentDisplayService.getDetails(id).map(rs -> {
            if (DataUtil.isNullOrEmpty(rs.getId())) {
                rs = null;
            }
            return ResponseEntity.ok(new DataResponse<>("success", rs));
        });
    }
}
