package com.ezbuy.orderservice.client;

import com.ezbuy.authmodel.dto.response.TenantIdentifyDTO;
import com.ezbuy.authmodel.model.Organization;
import com.ezbuy.authmodel.model.TenantIdentify;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface AuthClient {

    Mono<List<TenantIdentifyDTO>> getTenantIdentify(String organizationId);

    Mono<Organization> getOrganizationByIndividualId(String individualId);

    Mono<List<String>> getTrustedIdNoOrganization(String organizationId);

    Mono<String> findIndividualIdByUserIdAndOrganizationId(String userId, String organizationId);

    Mono<Optional<String>> findUserIdByIndividualIdAndActive(String individualId);

    /**
     * Ham lay individual id theo user dang nhap
     * @param userId
     * @param organizationId
     * @return
     */
    Mono<String> findIndividualIdByUserId(String userId, String organizationId);

    /**
     * Ham update tenant trust status theo id
     * @param request
     * @return
     */
    Mono<String> updateTrustStatusByTenantId(UpdateTenantTrustStatusRequest request);

    Mono<List<TenantIdentify>>getByTypeAndTenantIdAndTrustStatus(String type, String tenantId, String trustStatus);

    /**
     * Ham update tenant trust status not sign va gui mail
     * @param request
     * @return
     */
    Mono<String> updateTrustStatusNotSign(UpdateTenantTrustStatusRequest request);

}
