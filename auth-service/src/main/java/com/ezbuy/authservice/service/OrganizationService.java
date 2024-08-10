package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.request.CreateOrganizationRequest;
import com.ezbuy.authmodel.dto.request.CreateOrganizationUnitRequest;
import com.ezbuy.authmodel.model.OrganizationUnit;
import com.ezbuy.framework.model.response.DataResponse;
import com.nimbusds.openid.connect.sdk.assurance.evidences.Organization;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface OrganizationService {

    Mono<DataResponse<OrganizationUnit>> createOrganizationUnit(
            CreateOrganizationUnitRequest request, String organizationId);

    Mono<Optional<Organization>> createOrganization(
            CreateOrganizationRequest request, String createUser, String individualId, boolean isTrustedIdentify);
}
