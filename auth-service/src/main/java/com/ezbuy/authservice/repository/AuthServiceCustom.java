package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.dto.response.PermissionPolicyDetailDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthServiceCustom {
    Mono<Integer> countPermissionPolicyDetail(String filter, Integer state, String sort);

    Flux<PermissionPolicyDetailDto> getPermissionPolicyDetail(
            String filter, Integer state, Integer offset, Integer pageSize, String sort);

    // Flux<OrganizationUnitDto> getOrganizationUnitMasterAndChild(List<String>
    // organizationUnitId);
    //
    // Flux<OrganizationUnitDto> getOrganizationUnitAndChild(List<String>
    // organizationUnitId, Integer state);
    //
    // Flux<OrganizationUnitDto> getOrganizationUnit(List<String>
    // organizationUnitId);
    //
    // Flux<OrganizationUnitDto> getOrganizationUnitChildData(String
    // organizationUnitId, Integer state);
    //
    // Mono<OrganizationUnitDetailDto> getOrganizationUnitParent(String
    // organizationUnitId, String organizationId,
    // Integer state);
    //
    // Mono<OrganizationUnitDetailDto> getOrganizationUnitDetail(String
    // organizationUnitId, String organizationId);
    //
    // Flux<OrganizationUnitDetailDto> getOrganizationUnitDetailChild(String
    // parentId, String organizationId,Integer
    // state);
    //
    // public Flux<OrganizationUnitDetailDto>
    // getOrganizationUnitDetailChildAll(String parentId, String
    // organizationId);
    //
    // Mono<OrganizationUnitDetailDto> getOrganizationUnitDetailChildById(String id,
    // String organizationId);
    //
    // Flux<IndividualUnitPositionDto> getIndividualByOrganizationIdAndUserId(String
    // organizationId, String userId,
    // Integer state);
    //
    // Flux<IndividualShortDto> getLeaderByEmail(String email, String
    // organizationId);
    //
    // Mono<IndividualShortDto> getLeaderById(String leaderId, String
    // organizationId);
    //
    // Flux<OrganizationUnitDto> getOrganizationUnitAndChild(List<String>
    // organizationUnitId);
    //
    // /**
    // * Ham lay danh sach cay don vi theo ma don vi cha
    // * @param organizationUnitId
    // * @return
    // */
    // Flux<OrganizationUnitDto>
    // getOrganizationUnitTreeByRootUnitIdList(List<String> organizationUnitId);
    //
    // /**
    // * Ham lay danh sach don vi hieu luc theo ma to chuc va ma nguoi dung
    // * @param organizationId
    // * @param userId
    // * @param state
    // * @return
    // */
    // public Flux<IndividualUnitPositionDto>
    // getIndividualUnitpositionsActiveByOrganizationIdAndUserId(String
    // organizationId, String userId, Integer state);

}
