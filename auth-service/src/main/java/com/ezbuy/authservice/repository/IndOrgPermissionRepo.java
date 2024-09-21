package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.dto.OrgIndIdDTO;
import com.ezbuy.authmodel.model.PermissionPolicy;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IndOrgPermissionRepo extends R2dbcRepository<PermissionPolicy, String> {
    @Query(
            value = "select pp.id as id, pp.type as type, pp.policy_id as policy_id\n"
                    + "from individual_organization_permissions iop\n" + "         left join permission_policy pp\n"
                    + "                   on iop.id = pp.individual_organization_permissions_id\n"
                    + "         join individual i on i.id = iop.individual_id\n"
                    + "where iop.organization_id = :orgId\n"
                    + "  and i.user_id = :userId\n" + "  and iop.status = 1\n" + "  and pp.status = 1\n"
                    + "  and i.status = 1")
    Flux<PermissionPolicy> getAllByUserId(String orgId, String userId);

    @Query(
            value = "select count(distinct (iop.organization_id))\n" + "from individual_organization_permissions iop\n"
                    + "         inner join individual i on i.id = iop.individual_id\n" + "where i.status = 1\n"
                    + "  and iop.status = 1\n" + "  and i.user_id = :userId")
    Mono<Integer> getOrgIds(String userId);

    /**
     * get orgId, indId, userId
     *
     * @param offset
     *            number of offset
     * @param limit
     *            limit of moi offset
     * @return
     */
    @Query(
            value = "select organization_id org_id, individual_id individual_id, user_id\n"
                    + "from individual_unit_position iup\n" + "         join individual i on iup.individual_id = i.id\n"
                    + "where user_id in (select id from sme_keycloak.USER_ENTITY)\n"
                    + "and i.status = 1 and iup.status = 1\n"
                    + "group by individual_id, i.create_at\n" + "order by i.create_at\n"
                    + "limit :limit offset :offset")
    Flux<OrgIndIdDTO> getOrgIndId(Integer offset, Integer limit);

    /**
     * Check exist in HUB
     *
     * @param individualId
     *            id of individual
     * @param keycloakName
     *            name of role or group in keycloak
     * @param clientId
     *            id of dich vu
     * @param policyId
     *            policyId in keycloak
     * @param type
     *            type of policy (role, group)
     * @return
     */
    @Query(
            value =
                    "select exists(select * from individual_organization_permissions iop\n"
                            + "join permission_policy pp on iop.id = pp.individual_organization_permissions_id\n"
                            + "where individual_id = :individualId and keycloak_name = :keycloakName and client_id = :clientId and policy_id = :policyId\n"
                            + "and organization_id = :orgId and keycloak_id = :keycloakId and type = :type and iop.status = 1 and pp.status = 1)")
    Mono<Boolean> checkExistRoleInHub(
            String individualId,
            String keycloakName,
            String clientId,
            String policyId,
            String type,
            String orgId,
            String keycloakId);

    /**
     * get orgId, indId, userId by username
     *
     * @param username
     *            username
     * @return
     */
    @Query(
            value = "select organization_id org_id, individual_id individual_id, user_id\n"
                    + "from individual_unit_position iup\n" + "         join individual i on iup.individual_id = i.id\n"
                    + "where i.username = :username\n" + "and i.status = 1 and iup.status = 1\n"
                    + "group by individual_id, i.create_at\n" + "order by i.create_at")
    Flux<OrgIndIdDTO> getOrgIndIdByUsername(String username);

    /**
     * Get id of individual_organization_permissions if exist
     *
     * @param clientId
     *            id of dich vu
     * @param individualId
     *            id of user
     * @return
     */
    @Query(
            value = "select id from individual_organization_permissions\n" + "where client_id = :clientId\n"
                    + "and status = 1\n" + "and individual_id = :individualId\n" + "limit 1")
    Flux<String> getIndOrgPerIdByClientIdAndIndId(String clientId, String individualId);
}
