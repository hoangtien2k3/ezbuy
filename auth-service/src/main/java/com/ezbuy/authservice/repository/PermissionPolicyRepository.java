package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.model.PermissionPolicy;
import java.util.List;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PermissionPolicyRepository extends R2dbcRepository<PermissionPolicy, UUID> {
    @Query(
            value =
                    "SELECT pp.* FROM permission_policy pp  WHERE individual_organization_permissions_id = :individualOrgPerId AND status = :status ORDER BY :orderName :orderType")
    Flux<PermissionPolicy> getPermissionPolicy(
            String individualOrgPerId, String status, String orderName, String orderType);

    @Query(value = "UPDATE permission_policy pp SET STATE =:state  WHERE id = :id")
    void deleteIndividualPermission(String id, Integer state);

    Mono<PermissionPolicy> getPermissionPolicyById(String id);

    @Query(
            value = "SELECT pp.* FROM permission_policy pp, individual_organization_permission iop"
                    + " WHERE pp.individual_organization_permission_id = iop.id "
                    + " and iop.individual_id = :individualId "
                    + " and iop.organization_id = :organizationId " + " and pp.status = 1 ")
    Mono<List<PermissionPolicy>> getPermissionPolicyByIndividualAndOrganization(
            String individualId, String organizationId);

    @Query(
            value =
                    "select * from permission_policy WHERE individual_organization_permissions_id in (:individualOrgPerIdList)")
    Flux<PermissionPolicy> getByIndividualOrgPermissionId(List<String> individualOrgPerIdList);

    @Modifying
    @Query(
            value =
                    "UPDATE permission_policy pp SET status = :status  WHERE individual_organization_permissions_id in (:individualOrgPerIdList)")
    Mono<Integer> updateStatusByIndividualOrgPerIdList(List<String> individualOrgPerIdList, Integer status);

    @Query(
            value =
                    "delete from permission_policy  WHERE individual_organization_permissions_id in (:individualOrgPerIdList)")
    Mono<Integer> deleteByIndividualOrgPerIdList(List<String> individualOrgPerIdList);

    @Query(
            value =
                    "select * from permission_policy WHERE individual_organization_permissions_id in (select id from individual_organization_permissions where individual_id = :individualId and organization_id <> :organizationId)")
    Flux<PermissionPolicy> getNotOfOrganization(String individualId, String organizationId);

    @Query(
            value =
                    "select * from permission_policy WHERE individual_organization_permissions_id = :individualOrgPerId and status = 1")
    Flux<PermissionPolicy> findByIndividualOrgPerId(String individualOrgPerId);
}
