package com.ezbuy.authservice.controller;

import com.ezbuy.authmodel.constants.UrlPaths;
import com.ezbuy.authmodel.dto.request.JobAddRoleAdminForOldUserRequest;
import com.ezbuy.authservice.service.UtilService;
import com.ezbuy.reactify.model.response.DataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.Util.PREFIX)
public class UtilController {

    private final UtilService utilService;

    /**
     * Add role admin of EzBuy for old user
     *
     * @param request
     * @return
     */
    @PostMapping(value = UrlPaths.Util.JOB_ADD_ROLE_ADMIN_FOR_OLD_USER)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse<Object>>> jobAddRoleAdminForOldUser(
            @Valid @RequestBody JobAddRoleAdminForOldUserRequest request) {
        return utilService
                .jobAddRoleAdminForOldUser(request)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("common.success", rs.getData())));
    }
}
