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
import com.ezbuy.settingmodel.dto.PageDTO;
import com.ezbuy.settingmodel.model.Page;
import com.ezbuy.settingmodel.request.ChangePageStatusRequest;
import com.ezbuy.settingmodel.request.PageCreatingRequest;
import com.ezbuy.settingmodel.request.PagePolicyRequest;
import com.ezbuy.settingmodel.request.SearchingPageRequest;
import com.ezbuy.settingmodel.response.SearchingPageResponse;
import com.ezbuy.settingservice.service.PageService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.Page.PREFIX)
public class PageController {
    private final PageService pageService;

    @GetMapping()
    public Mono<DataResponse<PageDTO>> getPage(String code) {
        return this.pageService.getPage(code);
    }

    @GetMapping(UrlPaths.Page.SEARCH_PAGES)
    public Mono<ResponseEntity<DataResponse<SearchingPageResponse>>> searchPages(
            @ModelAttribute SearchingPageRequest request) {
        return this.pageService.searchPages(request).map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.Page.PAGE_DETAIL)
    public Mono<ResponseEntity<DataResponse<PageDTO>>> getDetailPage(@PathVariable(name = "id") String id) {
        return this.pageService.getDetailPage(id).map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('admin')")
    public Mono<ResponseEntity<DataResponse<Page>>> createPage(@Valid @RequestBody PageCreatingRequest request) {
        return this.pageService.createPage(request).map(ResponseEntity::ok);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('admin')")
    public Mono<ResponseEntity<DataResponse<Page>>> editPage(@Valid @RequestBody PageCreatingRequest request) {
        return this.pageService.editPage(request).map(ResponseEntity::ok);
    }

    @GetMapping(UrlPaths.Page.POLICY_PAGES)
    public Mono<ResponseEntity<DataResponse<String>>> policyPage(PagePolicyRequest request) {
        return this.pageService.policyPage(request).map(ResponseEntity::ok);
    }

    @PutMapping(UrlPaths.Page.CHANGE_STATUS)
    @PreAuthorize("hasAnyAuthority('admin')")
    public Mono<ResponseEntity<DataResponse<PageDTO>>> changeStatus(
            @Valid @RequestBody ChangePageStatusRequest request) {
        return this.pageService.changeStatus(request).map(ResponseEntity::ok);
    }
}
