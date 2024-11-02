package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.request.CreateOrganizationRequest;
import com.ezbuy.authmodel.dto.request.CreateOrganizationUnitRequest;
import com.ezbuy.authmodel.model.OrganizationUnit;
import com.nimbusds.openid.connect.sdk.assurance.evidences.Organization;
import com.ezbuy.reactify.model.response.DataResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface OrganizationService {

    Mono<DataResponse<OrganizationUnit>> createOrganizationUnit(
            CreateOrganizationUnitRequest request, String organizationId);

    Mono<Optional<Organization>> createOrganization(
            CreateOrganizationRequest request, String createUser, String individualId, boolean isTrustedIdentify);
}
