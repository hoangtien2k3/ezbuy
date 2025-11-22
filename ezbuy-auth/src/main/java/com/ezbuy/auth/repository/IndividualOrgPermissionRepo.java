package com.ezbuy.auth.repository;

import com.ezbuy.auth.model.entity.IndividualOrganizationPermissionsEntity;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IndividualOrgPermissionRepo extends R2dbcRepository<IndividualOrganizationPermissionsEntity, UUID> {
    @Query("""
            SELECT *
            FROM individual_organization_permissions
            WHERE individual_id = :individualId
              AND organization_id = :organizationId
              AND status = 1
            """)
    Flux<IndividualOrganizationPermissionsEntity> findByIndividualIdAndOrgId(String individualId, String organizationId);

    @Modifying
    @Query("""
            UPDATE individual_organization_permissions
            SET status = -1,
                update_at = NOW(),
                update_by = :userUpdate
            WHERE individual_id = :individualId
              AND organization_id = :organizationId
            """)
    Mono<Long> updateByIndividualIdAndOrgId(String individualId, String organizationId, String userUpdate);

    @Modifying
    @Query("""
            DELETE FROM individual_organization_permissions
            WHERE individual_id = :individualId
              AND organization_id = :organizationId
            """)
    Mono<Integer> deleteByIndividualIdAndOrgId(String individualId, String organizationId);

    @Query("""
            SELECT code
            FROM individual_organization_permissions iop
                     JOIN permission_policy pp ON iop.id = pp.individual_organization_permissions_id
            WHERE type = 'ROLE'
              AND organization_id = :orgId
              AND user_id = :userId
              AND client_id = :clientId
            """)
    Flux<String> getRolesByOrgIdUserIdClientId(String orgId, String userId, String clientId);

    @Query("""
            SELECT pp.keycloak_id
            FROM individual_organization_permissions iop
                     JOIN permission_policy pp ON iop.id = pp.individual_organization_permissions_id
            WHERE type = 'GROUP'
              AND individual_id = :individualId
              AND client_id = :clientId
            """)
    Flux<String> getGroupsByOrgIdUserIdClientId(String individualId, String clientId);

    @Query("""
            SELECT *
            FROM individual_organization_permissions
            WHERE individual_id = :individualId
              AND organization_id = :organizationId
              AND client_id = :clientId
              AND status = 1
            """)
    Mono<IndividualOrganizationPermissionsEntity> findByIndividualIdAndOrgIdAAndClientId(
            String individualId, String organizationId, String clientId);
}
