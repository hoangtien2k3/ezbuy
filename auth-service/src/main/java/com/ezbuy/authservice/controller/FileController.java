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
import io.hoangtien2k3.commons.model.response.DataResponse;
import io.hoangtien2k3.commons.utils.MinioUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(UrlPaths.FILE.PREFIX)
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class FileController {

    private final MinioUtils minioUtils;

    @Value("${minio.bucket}")
    private String bucket;

    @GetMapping(UrlPaths.FILE.DOWNLOAD)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<?>>> download(@RequestParam String filePath) {
        return Mono.just(minioUtils.getBase64FromUrl(bucket, filePath))
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }
}
