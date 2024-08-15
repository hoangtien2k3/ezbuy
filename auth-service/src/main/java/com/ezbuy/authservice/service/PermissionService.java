package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.response.OrgPermission;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PermissionService {
    Mono<List<OrgPermission>> getOrgPermissions();
}
