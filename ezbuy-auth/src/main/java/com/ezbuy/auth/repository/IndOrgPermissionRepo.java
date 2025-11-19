package com.ezbuy.auth.repository;

import com.ezbuy.auth.model.dto.OrgIndIdDTO;
import com.ezbuy.auth.model.entity.PermissionPolicyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IndOrgPermissionRepo extends R2dbcRepository<PermissionPolicyEntity, String> {
    @Query("""
            SELECT pp.id AS id,
                   pp.type AS type,
                   pp.policy_id AS policy_id
            FROM individual_organization_permissions iop
                     LEFT JOIN permission_policy pp
                               ON iop.id = pp.individual_organization_permissions_id
                     JOIN individual i ON i.id = iop.individual_id
            WHERE iop.organization_id = :orgId
              AND i.user_id = :userId
              AND iop.status = 1
              AND pp.status = 1
              AND i.status = 1
            """)
    Flux<PermissionPolicyEntity> getAllByUserId(String orgId, String userId);

    @Query("""
            SELECT COUNT(DISTINCT iop.organization_id)
            FROM individual_organization_permissions iop
                     INNER JOIN individual i ON i.id = iop.individual_id
            WHERE i.status = 1
              AND iop.status = 1
              AND i.user_id = :userId
            """)
    Mono<Integer> getOrgIds(String userId);

    @Query("""
            SELECT organization_id AS org_id,
                   individual_id AS individual_id,
                   user_id
            FROM individual_unit_position iup
                     JOIN individual i ON iup.individual_id = i.id
            WHERE user_id IN (SELECT id FROM USER_ENTITY)
              AND i.status = 1
              AND iup.status = 1
            GROUP BY individual_id, i.create_at
            ORDER BY i.create_at
            LIMIT :limit OFFSET :offset
            """)
    Flux<OrgIndIdDTO> getOrgIndId(Integer offset, Integer limit);

    @Query("""
            SELECT EXISTS(
                SELECT *
                FROM individual_organization_permissions iop
                         JOIN permission_policy pp
                           ON iop.id = pp.individual_organization_permissions_id
                WHERE individual_id = :individualId
                  AND keycloak_name = :keycloakName
                  AND client_id = :clientId
                  AND policy_id = :policyId
                  AND organization_id = :orgId
                  AND keycloak_id = :keycloakId
                  AND type = :type
                  AND iop.status = 1
                  AND pp.status = 1
            )
            """)
    Mono<Boolean> checkExistRoleInHub(
            String individualId,
            String keycloakName,
            String clientId,
            String policyId,
            String type,
            String orgId,
            String keycloakId);

    @Query("""
            SELECT organization_id AS org_id,
                   individual_id AS individual_id,
                   user_id
            FROM individual_unit_position iup
                     JOIN individual i ON iup.individual_id = i.id
            WHERE i.username = :username
              AND i.status = 1
              AND iup.status = 1
            GROUP BY individual_id, i.create_at
            ORDER BY i.create_at
            """)
    Flux<OrgIndIdDTO> getOrgIndIdByUsername(String username);

    @Query("""
            SELECT id
            FROM individual_organization_permissions
            WHERE client_id = :clientId
              AND status = 1
              AND individual_id = :individualId
            LIMIT 1
            """)
    Flux<String> getIndOrgPerIdByClientIdAndIndId(String clientId, String individualId);
}
