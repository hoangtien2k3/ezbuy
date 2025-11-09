package com.ezbuy.auth.controller;

import com.ezbuy.authmodel.constants.UrlPaths;
import com.ezbuy.authmodel.dto.request.ActionLogRequest;
import com.ezbuy.auth.service.ActionLogService;
import com.ezbuy.core.model.response.DataResponse;
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

    @GetMapping(value = UrlPaths.ActionLog.EXPORT_EXCEL)
    public Mono<ResponseEntity<Resource>> getExportFile(ActionLogRequest request) {
        return actionLogService.exportUser(request).map(resource -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .body(resource));
    }
}
