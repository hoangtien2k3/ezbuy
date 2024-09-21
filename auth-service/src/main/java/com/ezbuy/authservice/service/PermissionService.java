package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.response.OrgPermission;
import java.util.List;
import reactor.core.publisher.Mono;

public interface PermissionService {
    Mono<List<OrgPermission>> getOrgPermissions();
}
