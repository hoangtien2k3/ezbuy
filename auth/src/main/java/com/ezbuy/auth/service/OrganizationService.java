package com.ezbuy.auth.service;

import java.util.Optional;

import com.nimbusds.openid.connect.sdk.assurance.evidences.Organization;

import com.ezbuy.auth.model.dto.request.CreateOrganizationRequest;
import com.ezbuy.auth.model.dto.request.CreateOrganizationUnitRequest;
import com.ezbuy.auth.model.postgresql.OrganizationUnit;
import com.ezbuy.framework.model.response.DataResponse;

import reactor.core.publisher.Mono;

public interface OrganizationService {

    Mono<DataResponse<OrganizationUnit>> createOrganizationUnit(
            CreateOrganizationUnitRequest request, String organizationId);

    Mono<Optional<Organization>> createOrganization(
            CreateOrganizationRequest request, String createUser, String individualId, boolean isTrustedIdentify);
}
