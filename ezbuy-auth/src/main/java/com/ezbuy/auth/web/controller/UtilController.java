package com.ezbuy.auth.web.controller;

import com.ezbuy.auth.application.dto.request.JobAddRoleAdminForOldUserRequest;
import com.ezbuy.auth.application.service.UtilService;
import com.ezbuy.core.model.response.DataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth/util")
public class UtilController {

    private final UtilService utilService;

    @PostMapping("/add-role-admin-for-old-user")
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse>> jobAddRoleAdminForOldUser(
            @Valid @RequestBody JobAddRoleAdminForOldUserRequest request) {
        return utilService
                .jobAddRoleAdminForOldUser(request)
                .map(rs -> ResponseEntity.ok(new DataResponse("common.success", rs.getData())));
    }
}
