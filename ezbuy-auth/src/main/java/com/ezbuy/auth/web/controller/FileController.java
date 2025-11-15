package com.ezbuy.auth.web.controller;

import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.MinioUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/v1/auth/file")
@RequiredArgsConstructor
public class FileController {
    private final MinioUtils minioUtils;

    @Value("${minio.bucket}")
    private String bucket;

    @GetMapping("/download")
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<ResponseEntity<DataResponse<?>>> download(@RequestParam String filePath) {
        return Mono.just(minioUtils.getBase64FromUrl(bucket, filePath))
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs)));
    }
}
