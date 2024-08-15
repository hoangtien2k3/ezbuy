package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.request.CreateIndPermistionRequest;
import com.ezbuy.authmodel.dto.response.PermissionPolicyDetailDto;
import com.ezbuy.authmodel.model.IndividualOrganizationPermissions;
import com.ezbuy.authmodel.model.PermissionPolicy;
import com.ezbuy.framework.model.TokenUser;
import com.ezbuy.framework.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PermissionPolicyService {
    Mono<DataResponse> getPermissionPolicyDto(String filter, Integer state, Integer pageIndex, Integer pageSize, String sort);
    Mono<DataResponse<Boolean>> deletePermissionPolicy(String permissionPolicyId);
    Mono<DataResponse<Boolean>> addOrUpdatePermissionPolicy(CreateIndPermistionRequest request);
    Mono<DataResponse<PermissionPolicyDetailDto>> getTelecomServiceByOrg(UUID organizationId);

    Mono<PermissionPolicy> createPermissionPolicy(String type, String keycloakId, String keycloakName, String policyId, IndividualOrganizationPermissions individualOrganizationPermissions, LocalDateTime now, TokenUser currentUser);
}
