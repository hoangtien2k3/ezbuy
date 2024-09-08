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
import com.ezbuy.settingmodel.dto.request.SearchGroupNewsRequest;
import com.ezbuy.settingmodel.model.GroupNews;
import com.ezbuy.settingmodel.request.CreateGroupNewsRequest;
import com.ezbuy.settingmodel.response.SearchGroupNewsResponse;
import com.ezbuy.settingservice.service.GroupNewsService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(UrlPaths.GroupNews.PREFIX)
@RequiredArgsConstructor
public class GroupNewsController {
    private final GroupNewsService groupNewsService;

    @PostMapping
    public Mono<DataResponse<GroupNews>> createGroupNews(@RequestBody CreateGroupNewsRequest request) {
        return groupNewsService.createGroupNews(request);
    }

    @PutMapping(value = UrlPaths.GroupNews.UPDATE)
    public Mono<DataResponse<GroupNews>> editGroupNews(
            @PathVariable String id, @Valid @RequestBody CreateGroupNewsRequest request) {
        return groupNewsService.editGroupNews(id, request);
    }

    @GetMapping
    public Mono<SearchGroupNewsResponse> findGroupNews(@ModelAttribute SearchGroupNewsRequest request) {
        return groupNewsService.findGroupNews(request);
    }

    @GetMapping(UrlPaths.GroupNews.ALL)
    public Mono<DataResponse<List<GroupNews>>> getAllGroupNews() {
        return groupNewsService.getAllGroupNews().map(rs -> new DataResponse<>("success", rs));
    }
}
