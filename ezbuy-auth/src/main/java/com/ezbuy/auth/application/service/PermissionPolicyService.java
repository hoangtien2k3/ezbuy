package com.ezbuy.auth.application.service;

import com.ezbuy.auth.application.dto.request.CreateIndPermistionRequest;
import com.ezbuy.auth.application.dto.response.PermissionPolicyDetailDto;
import com.ezbuy.auth.application.dto.response.SearchPermissionPolicyResponse;
import com.ezbuy.auth.domain.model.entity.IndividualOrganizationPermissionsEntity;
import com.ezbuy.auth.domain.model.entity.PermissionPolicyEntity;
import com.ezbuy.core.model.TokenUser;
import com.ezbuy.core.model.response.DataResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface PermissionPolicyService {
    Mono<DataResponse<SearchPermissionPolicyResponse>> getPermissionPolicyDto(
            String filter, Integer state, Integer pageIndex, Integer pageSize, String sort);

    Mono<DataResponse<Boolean>> deletePermissionPolicy(String permissionPolicyId);

    Mono<DataResponse<Boolean>> addOrUpdatePermissionPolicy(CreateIndPermistionRequest request);

    Mono<DataResponse<PermissionPolicyDetailDto>> getTelecomServiceByOrg(UUID organizationId);

    Mono<PermissionPolicyEntity> createPermissionPolicy(
            String type,
            String keycloakId,
            String keycloakName,
            String policyId,
            IndividualOrganizationPermissionsEntity individualOrganizationPermissions,
            LocalDateTime now,
            TokenUser currentUser);
}
