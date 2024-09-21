package com.ezbuy.authservice.service.impl;

import com.ezbuy.authmodel.dto.response.OrgPermission;
import com.ezbuy.authservice.service.PermissionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    @Override
    public Mono<List<OrgPermission>> getOrgPermissions() {
        return null;
    }
}
