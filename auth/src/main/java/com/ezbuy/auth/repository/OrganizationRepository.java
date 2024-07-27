package com.ezbuy.auth.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nimbusds.openid.connect.sdk.assurance.evidences.Organization;

import reactor.core.publisher.Mono;

@Repository
public interface OrganizationRepository extends R2dbcRepository<Organization, UUID> {
    @Query(value = "SELECT * FROM organization o WHERE o.id = :organizationId AND o.status = :status")
    Mono<Organization> getOrganizationByIdAndStatus(
            @Param("organizationId") String organizationId, @Param("status") Integer status);
}
