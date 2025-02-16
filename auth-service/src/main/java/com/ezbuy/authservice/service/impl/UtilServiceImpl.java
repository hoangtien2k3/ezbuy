package com.ezbuy.authservice.service.impl;

import com.ezbuy.authmodel.dto.request.EmployeePermissionRequest;
import com.ezbuy.authmodel.dto.request.JobAddRoleAdminForOldUserRequest;
import com.ezbuy.authmodel.model.Individual;
import com.ezbuy.authmodel.model.IndividualOrganizationPermissions;
import com.ezbuy.authmodel.model.PermissionPolicy;
import com.ezbuy.authservice.client.KeyCloakClient;
import com.ezbuy.authservice.config.KeycloakProvider;
import com.ezbuy.authservice.repository.IndOrgPermissionRepo;
import com.ezbuy.authservice.repository.IndividualOrgPermissionRepo;
import com.ezbuy.authservice.service.PermissionPolicyService;
import com.ezbuy.authservice.service.UtilService;
import com.reactify.constants.Constants;
import com.reactify.model.TokenUser;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
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
    // private final IndividualService individualService;
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
                .flatMap(rs -> Mono.just(DataResponse.success("Số bản ghi thành công " + rs.size())));
    }

    private static List<EmployeePermissionRequest> getEmployeePermissionRequests(
            JobAddRoleAdminForOldUserRequest request) {
        List<EmployeePermissionRequest> employeePermissionRequests = new ArrayList<>();
        EmployeePermissionRequest employeePermissionRequest = new EmployeePermissionRequest();
        employeePermissionRequest.setRoleId(request.getRoleId());
        employeePermissionRequest.setClientId(request.getClientId());
        employeePermissionRequest.setRoleCode(request.getRoleName());
        employeePermissionRequest.setPolicyId(request.getPolicyId());
        employeePermissionRequests.add(employeePermissionRequest);
        return employeePermissionRequests;
    }

    private Mono<String> getAccessToken() {
        return Mono.fromCallable(
                () -> keycloakProvider.getInstance().tokenManager().getAccessTokenString());
    }

    // save role in 2 table individualOrgPermissionRepo va
    public Mono<List<PermissionPolicy>> createEmployeePermission(
            List<EmployeePermissionRequest> employeeUpdateRequest, Individual individual, String organizationId) {
        return Flux.fromIterable(employeeUpdateRequest)
                .flatMap(employeePermissionRequest -> indOrgPermissionRepo
                        .getIndOrgPerIdByClientIdAndIndId(employeePermissionRequest.getClientId(), individual.getId())
                        .collectList()
                        .flatMap(rs -> {
                            TokenUser tokenUser = new TokenUser();
                            tokenUser.setUsername("system");
                            IndividualOrganizationPermissions individualOrganizationPermissions =
                                    new IndividualOrganizationPermissions();
                            if (rs.isEmpty()) {
                                individualOrganizationPermissions.setId(String.valueOf(UUID.randomUUID()));
                                individualOrganizationPermissions.setIndividualId(individual.getId());
                                individualOrganizationPermissions.setOrganizationId(organizationId);
                                individualOrganizationPermissions.setClientId(employeePermissionRequest.getClientId());
                                individualOrganizationPermissions.setCreateAt(LocalDateTime.now());
                                individualOrganizationPermissions.setUpdateAt(LocalDateTime.now());
                                individualOrganizationPermissions.setCreateBy("system");
                                individualOrganizationPermissions.setUpdateBy("system");
                                individualOrganizationPermissions.setStatus(Constants.STATUS.ACTIVE);
                                return individualOrgPermissionRepo
                                        .save(individualOrganizationPermissions)
                                        .flatMap(data -> permissionPolicyService.createPermissionPolicy(
                                                Constants.PERMISSION_TYPE.ROLE,
                                                employeePermissionRequest.getRoleId(),
                                                employeePermissionRequest.getRoleCode(),
                                                employeePermissionRequest.getPolicyId(),
                                                individualOrganizationPermissions,
                                                LocalDateTime.now(),
                                                tokenUser));
                            }
                            individualOrganizationPermissions.setId(rs.getFirst());
                            return permissionPolicyService.createPermissionPolicy(
                                    Constants.PERMISSION_TYPE.ROLE,
                                    employeePermissionRequest.getRoleId(),
                                    employeePermissionRequest.getRoleCode(),
                                    employeePermissionRequest.getPolicyId(),
                                    individualOrganizationPermissions,
                                    LocalDateTime.now(),
                                    tokenUser);
                        }))
                .collectList();
    }
}
