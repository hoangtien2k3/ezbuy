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
package com.ezbuy.authservice.controller;

import com.ezbuy.authmodel.constants.UrlPaths;
import com.ezbuy.authmodel.dto.request.ActionLogRequest;
import com.ezbuy.authservice.service.ActionLogService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.ActionLog.PREFIX)
public class ActionLogController {

    private final ActionLogService actionLogService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin')")
    public Mono<ResponseEntity<DataResponse<?>>> search(ActionLogRequest request) {
        return actionLogService
                .search(request)
                .flatMap(rs -> Mono.just(ResponseEntity.ok(new DataResponse<>("common.success", rs))));
    }

    @GetMapping("/excel")
    public Mono<ResponseEntity<Resource>> getExportFile(ActionLogRequest request) {
        return actionLogService.exportUser(request).map(resource -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .body(resource));
    }
}
