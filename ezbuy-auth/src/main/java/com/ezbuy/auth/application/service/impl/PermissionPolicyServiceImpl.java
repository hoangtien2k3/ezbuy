package com.ezbuy.auth.application.service.impl;

import com.ezbuy.auth.shared.constants.State;
import com.ezbuy.auth.application.dto.PaginationDTO;
import com.ezbuy.auth.application.dto.request.CreateIndPermistionRequest;
import com.ezbuy.auth.application.dto.response.PermissionPolicyDetailDto;
import com.ezbuy.auth.application.dto.response.SearchPermissionPolicyResponse;
import com.ezbuy.auth.domain.model.entity.IndividualOrganizationPermissionsEntity;
import com.ezbuy.auth.domain.model.entity.PermissionPolicyEntity;
import com.ezbuy.auth.infrastructure.repository.AuthServiceCustom;
import com.ezbuy.auth.infrastructure.repository.PermissionPolicyRepository;
import com.ezbuy.auth.application.service.PermissionPolicyService;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.constants.Constants;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.TokenUser;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.MessageUtils;
import com.ezbuy.core.util.PageUtils;
import com.ezbuy.core.util.SecurityUtils;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class PermissionPolicyServiceImpl implements PermissionPolicyService {
    private final AuthServiceCustom authServiceCustom;
    private final PermissionPolicyRepository permissionPolicyRepository;

    @Override
    public Mono<DataResponse<SearchPermissionPolicyResponse>> getPermissionPolicyDto(
            String filter, Integer state, Integer pageIndex, Integer pagesize, String sort) {
        int size = DataUtil.safeToInt(pagesize, 0);
        if (size == 0) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, MessageUtils.getMessage("size.invalid")));
        }
        if (size < 0 || size > 100) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, MessageUtils.getMessage("size.exceed", 100)));
        }
        int page = DataUtil.safeToInt(pageIndex, 0);
        if (page == 0) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, MessageUtils.getMessage("page.invalid")));
        }
        if (page < 0) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, MessageUtils.getMessage("page.exceed", 1)));
        }
        int offset = PageUtils.getOffset(page, size);
        Mono<Integer> countTotal = authServiceCustom.countPermissionPolicyDetail(filter, state, sort);
        Flux<PermissionPolicyDetailDto> policyDetailDtoFlux =
                authServiceCustom.getPermissionPolicyDetail(filter, state, offset, pagesize, sort);

        return Mono.zip(countTotal, policyDetailDtoFlux.collectList()).flatMap(response -> {
            PaginationDTO pagination = PaginationDTO.builder()
                    .pageIndex(page)
                    .pageSize(size)
                    .totalRecords(response.getT1().longValue())
                    .build();
            SearchPermissionPolicyResponse permissionPolicyResponse = SearchPermissionPolicyResponse.builder()
                    .content(response.getT2())
                    .pagination(pagination)
                    .build();
            return Mono.just(new DataResponse<>("success", permissionPolicyResponse));
        });
    }

    @Override
    public Mono<DataResponse<Boolean>> deletePermissionPolicy(String permissionPolicyId) {
        try {
            if (DataUtil.isNullOrEmpty(permissionPolicyId)) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS,
                        MessageUtils.getMessage("input.invalid", "PermissionPolicyId")));
            }
            return permissionPolicyRepository
                    .getPermissionPolicyById(permissionPolicyId)
                    .flatMap(obj -> {
                        permissionPolicyRepository.deleteIndividualPermission(obj.getId(), Constants.STATE.INACTIVE);
                        return Mono.just(DataResponse.success(true));
                    })
                    .switchIfEmpty(Mono.error(new BusinessException(
                            CommonErrorCode.NOT_FOUND, MessageUtils.getMessage("data.not.found"))));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            Mono.error(new BusinessException(HttpStatus.BAD_REQUEST.toString(), ex.getMessage()));
        }
        return Mono.just(DataResponse.failed(false));
    }

    @Override
    public Mono<DataResponse<Boolean>> addOrUpdatePermissionPolicy(CreateIndPermistionRequest request) {
        if (DataUtil.isNullOrEmpty(request.getGroupPermissionName())) {
            Mono.error(new BusinessException(
                    HttpStatus.BAD_REQUEST.toString(), MessageUtils.getMessage("input.not.null", "Tên nhóm quyền")));
        }
        if (DataUtil.isNullOrEmpty(request.getGroupPermissionCode())) {
            Mono.error(new BusinessException(
                    HttpStatus.BAD_REQUEST.toString(), MessageUtils.getMessage("input.not.null", "Mã nhóm quyền")));
        }
        if (!request.getGroupPermissionCode().matches("^[a-z0-9_]{3,50}$")) {
            Mono.error(new BusinessException(
                    HttpStatus.BAD_REQUEST.toString(),
                    MessageUtils.getMessage("input.invalid.format", "Mã nhóm quyền")));
        }
        if (DataUtil.isNullOrEmpty(request.getState())) {
            Mono.error(new BusinessException(
                    HttpStatus.BAD_REQUEST.toString(), MessageUtils.getMessage("input.not.null", "Loại nhóm quyền")));
        }
        if (!State.statusOf(request.getState())) {
            Mono.error(new BusinessException(
                    HttpStatus.BAD_REQUEST.toString(), MessageUtils.getMessage("input.invalid", "Loại nhóm quyền")));
        }
        if (request.getDescription().length() > 500) {
            Mono.error(new BusinessException(
                    HttpStatus.BAD_REQUEST.toString(),
                    MessageUtils.getMessage("input.length.exceed", "Description", "500")));
        }
        addOrUpdateIndividualPermission(request);
        return Mono.just(DataResponse.success(true));
    }

    @Override
    public Mono<DataResponse<PermissionPolicyDetailDto>> getTelecomServiceByOrg(UUID organizationId) {
        return null;
    }

    @Override
    public Mono<PermissionPolicyEntity> createPermissionPolicy(
            String type,
            String keycloakId,
            String keycloakName,
            String policyId,
            IndividualOrganizationPermissionsEntity individualOrganizationPermissions,
            LocalDateTime now,
            TokenUser currentUser) {
        PermissionPolicyEntity permissionPolicy = new PermissionPolicyEntity();
        permissionPolicy.setId(String.valueOf(UUID.randomUUID()));
        permissionPolicy.setType(type);
        permissionPolicy.setKeycloakId(keycloakId);
        permissionPolicy.setKeycloakName(keycloakName);
        permissionPolicy.setPolicyId(policyId);
        permissionPolicy.setIndividualOrganizationPermissionsId(individualOrganizationPermissions.getId());
        permissionPolicy.setCreateAt(now);
        permissionPolicy.setUpdateAt(now);
        permissionPolicy.setCreateBy(currentUser.getUsername());
        permissionPolicy.setUpdateBy(currentUser.getUsername());
        permissionPolicy.setStatus(Constants.STATUS.ACTIVE);
        permissionPolicy.setNew(true);
        return permissionPolicyRepository.save(permissionPolicy);
    }

    private void addOrUpdateIndividualPermission(CreateIndPermistionRequest request) {
        List<String> actions = Arrays.asList("CREATE", "UPDATE");
        if (!DataUtil.isNullOrEmpty(request.getAction()) || !actions.contains(request.getAction())) {
            Mono.error(new BusinessException(
                    HttpStatus.BAD_REQUEST.toString(), MessageUtils.getMessage("input.invalid", "Action")));
        }
        PermissionPolicyEntity permissionPolicy = PermissionPolicyEntity.builder()
                .id(
                        "CREATE".equals(request.getAction())
                                ? UUID.randomUUID().toString()
                                : request.getIndividualPermissionsId().toString())
                .type(request.getType())
                // .value(request.getValue())
                .code(request.getGroupPermissionCode())
                .createAt(LocalDateTime.now())
                .createBy(SecurityUtils.getCurrentUser()
                        .map(TokenUser::getUsername)
                        .toString())
                .individualOrganizationPermissionsId(
                        request.getIndividualOrgPermissionsId().toString())
                .isNew("CREATE".equals(request.getAction()))
                .build();
        permissionPolicyRepository.save(permissionPolicy);
    }
}
