package com.ezbuy.auth.service.impl;

import com.ezbuy.authmodel.constants.AuthConstants;
import com.ezbuy.authmodel.dto.request.EmployeePermissionRequest;
import com.ezbuy.authmodel.dto.request.JobAddRoleAdminForOldUserRequest;
import com.ezbuy.authmodel.model.Individual;
import com.ezbuy.authmodel.model.IndividualOrganizationPermissions;
import com.ezbuy.authmodel.model.PermissionPolicy;
import com.ezbuy.auth.client.KeyCloakClient;
import com.ezbuy.auth.config.KeycloakProvider;
import com.ezbuy.auth.repository.IndOrgPermissionRepo;
import com.ezbuy.auth.repository.IndividualOrgPermissionRepo;
import com.ezbuy.auth.service.PermissionPolicyService;
import com.ezbuy.auth.service.UtilService;
import com.ezbuy.core.constants.Constants;
import com.ezbuy.core.model.TokenUser;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UtilServiceImpl implements UtilService {

    private final IndOrgPermissionRepo indOrgPermissionRepo;
    private final KeyCloakClient keyCloakClient;
    private final KeycloakProvider keycloakProvider;
    private final IndividualOrgPermissionRepo individualOrgPermissionRepo;
    private final PermissionPolicyService permissionPolicyService;

    @Override
    public Mono<DataResponse<Object>> jobAddRoleAdminForOldUser(JobAddRoleAdminForOldUserRequest request) {
        return getAccessToken()
                .flatMap(token -> {
                    var orgIndId = indOrgPermissionRepo.getOrgIndIdByUsername(request.getUsername());
                    if (DataUtil.isNullOrEmpty(request.getUsername())) {
                        Integer offset = request.getLimit() * request.getOffset();
                        orgIndId = indOrgPermissionRepo.getOrgIndId(offset, request.getLimit());
                    }
                    return orgIndId.flatMapSequential(orgIndIdDTO -> {
                                RoleRepresentation roleRepresentation = new RoleRepresentation();
                                roleRepresentation.setId(request.getRoleId());
                                roleRepresentation.setName(request.getRoleName());
                                roleRepresentation.setComposite(Boolean.FALSE);
                                roleRepresentation.setClientRole(Boolean.TRUE);
                                roleRepresentation.setContainerId(request.getClientId());
                                return keyCloakClient
                                        .addRoleForUserInClientId(
                                                request.getClientId(),
                                                token,
                                                roleRepresentation,
                                                orgIndIdDTO.getUserId())
                                        .flatMap(result -> {
                                            List<EmployeePermissionRequest> employeePermissionRequests =
                                                    getEmployeePermissionRequests(request);
                                            Individual individual = new Individual();
                                            individual.setId(orgIndIdDTO.getIndividualId());
                                            return indOrgPermissionRepo
                                                    .checkExistRoleInHub(
                                                            orgIndIdDTO.getIndividualId(),
                                                            request.getRoleName(),
                                                            request.getClientId(),
                                                            request.getPolicyId(),
                                                            "ROLE",
                                                            orgIndIdDTO.getOrgId(),
                                                            request.getRoleId())
                                                    .flatMap(exist -> {
                                                        if (Boolean.TRUE.equals(exist)) {
                                                            return Mono.just(true);
                                                        }
                                                        // save 2 table
                                                        return createEmployeePermission(
                                                                employeePermissionRequests,
                                                                individual,
                                                                orgIndIdDTO.getOrgId());
                                                    });
                                        });
                            })
                            .collectList();
                })
                .flatMap(rs -> Mono.just(DataResponse.success("success: " + rs.size())));
    }

    private static List<EmployeePermissionRequest> getEmployeePermissionRequests(
            JobAddRoleAdminForOldUserRequest request) {
        List<EmployeePermissionRequest> employeePermissionRequests = new ArrayList<>();
        employeePermissionRequests.add(EmployeePermissionRequest.builder()
                .roleId(request.getRoleId())
                .clientId(request.getClientId())
                .roleCode(request.getRoleName())
                .policyId(request.getPolicyId())
                .build());
        return employeePermissionRequests;
    }

    private Mono<String> getAccessToken() {
        return Mono.fromCallable(
                () -> keycloakProvider.getInstance().tokenManager().getAccessTokenString());
    }

    public Mono<List<PermissionPolicy>> createEmployeePermission(
            List<EmployeePermissionRequest> employeeUpdateRequest, Individual individual, String organizationId) {
        return Flux.fromIterable(employeeUpdateRequest)
                .flatMap(employeePr -> indOrgPermissionRepo
                        .getIndOrgPerIdByClientIdAndIndId(employeePr.getClientId(), individual.getId())
                        .collectList()
                        .flatMap(rs -> {
                            TokenUser tokenUser = new TokenUser();
                            tokenUser.setUsername("system");
                            IndividualOrganizationPermissions individualOrg = new IndividualOrganizationPermissions();
                            if (rs.isEmpty()) {
                                individualOrg.setId(String.valueOf(UUID.randomUUID()));
                                individualOrg.setIndividualId(individual.getId());
                                individualOrg.setOrganizationId(organizationId);
                                individualOrg.setClientId(employeePr.getClientId());
                                individualOrg.setCreateAt(LocalDateTime.now());
                                individualOrg.setUpdateAt(LocalDateTime.now());
                                individualOrg.setCreateBy("system");
                                individualOrg.setUpdateBy("system");
                                individualOrg.setStatus(Constants.STATUS.ACTIVE);
                                return individualOrgPermissionRepo
                                        .save(individualOrg)
                                        .flatMap(data -> permissionPolicyService.createPermissionPolicy(
                                                AuthConstants.PERMISSION_TYPE.ROLE,
                                                employeePr.getRoleId(),
                                                employeePr.getRoleCode(),
                                                employeePr.getPolicyId(),
                                                individualOrg,
                                                LocalDateTime.now(),
                                                tokenUser));
                            }
                            individualOrg.setId(rs.getFirst());
                            return permissionPolicyService.createPermissionPolicy(
                                    AuthConstants.PERMISSION_TYPE.ROLE,
                                    employeePr.getRoleId(),
                                    employeePr.getRoleCode(),
                                    employeePr.getPolicyId(),
                                    individualOrg,
                                    LocalDateTime.now(),
                                    tokenUser);
                        }))
                .collectList();
    }
}
