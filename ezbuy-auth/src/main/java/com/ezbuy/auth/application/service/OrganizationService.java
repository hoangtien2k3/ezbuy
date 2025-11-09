package com.ezbuy.auth.application.service;

import com.ezbuy.auth.application.dto.request.CreateOrganizationRequest;
import com.ezbuy.auth.application.dto.request.CreateOrganizationUnitRequest;
import com.ezbuy.auth.domain.model.entity.OrganizationUnitEntity;
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
