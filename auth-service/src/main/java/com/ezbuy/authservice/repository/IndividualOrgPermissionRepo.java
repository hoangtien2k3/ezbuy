/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.model.IndividualOrganizationPermissions;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IndividualOrgPermissionRepo extends R2dbcRepository<IndividualOrganizationPermissions, UUID> {
    @Query(
            value =
                    "select * from individual_organization_permissions where individual_id = :individualId and organization_id = :organizationId and status = 1")
    Flux<IndividualOrganizationPermissions> findByIndividualIdAndOrgId(String individualId, String organizationId);

    @Modifying
    @Query(
            value = "update\n" + "   individual_organization_permissions\n" + "set\n"
                    + "   status = -1, update_at = now(), update_by = :userUpdate \n" + "WHERE\n"
                    + "   individual_id = :individualId and organization_id = :organizationId")
    Mono<Long> updateByIndividualIdAndOrgId(String individualId, String organizationId, String userUpdate);

    @Modifying
    @Query(
            value = "delete from \n" + "   individual_organization_permissions \n" + "WHERE\n"
                    + "   individual_id = :individualId and organization_id = :organizationId")
    Mono<Integer> deleteByIndividualIdAndOrgId(String individualId, String organizationId);

    @Query(
            value = "select code\n" + "from individual_organization_permissions iop\n"
                    + "         join permission_policy pp\n"
                    + "              on iop.id = pp.individual_organization_permissions_id\n" + "where type = 'ROLE'\n"
                    + "  and organization_id = :orgId\n" + "  and user_id = :userId\n" + "  and client_id = :clientId")
    Flux<String> getRolesByOrgIdUserIdClientId(String orgId, String userId, String clientId);

    @Query(
            value = "select pp.keycloak_id\n" + "from individual_organization_permissions iop\n"
                    + "         join permission_policy pp\n"
                    + "              on iop.id = pp.individual_organization_permissions_id\n" + "where type = 'GROUP'\n"
                    + "  and individual_id = :individualId\n" + "  and client_id = :clientId")
    Flux<String> getGroupsByOrgIdUserIdClientId(String individualId, String clientId);

    @Query(
            value =
                    "select * from individual_organization_permissions where individual_id = :individualId and organization_id = :organizationId and client_id = :clientId and status = 1")
    Mono<IndividualOrganizationPermissions> findByIndividualIdAndOrgIdAAndClientId(
            String individualId, String organizationId, String clientId);
}
