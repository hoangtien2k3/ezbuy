package com.ezbuy.auth.service;

import com.ezbuy.auth.model.dto.request.CreateOrganizationRequest;
import com.ezbuy.auth.model.dto.request.CreateOrganizationUnitRequest;
import com.ezbuy.auth.model.entity.OrganizationUnitEntity;
import com.nimbusds.openid.connect.sdk.assurance.evidences.Organization;
import com.ezbuy.core.model.response.DataResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface OrganizationService {

    Mono<DataResponse<OrganizationUnitEntity>> createOrganizationUnit(
            CreateOrganizationUnitRequest request, String organizationId);

    Mono<Optional<Organization>> createOrganization(
            CreateOrganizationRequest request, String createUser, String individualId, boolean isTrustedIdentify);
}
