package com.ezbuy.productservice.client;

import com.ezbuy.authmodel.dto.response.GetActionLoginReportResponse;
import com.ezbuy.authmodel.dto.response.TenantIdentifyDTO;
import com.ezbuy.authmodel.model.OrganizationUnit;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface AuthClient {

    Mono<List<TenantIdentifyDTO>> getTenantIdentify(String organizationId);

    Mono<List<String>> getUsersTrusted();

    Mono<List<String>> getTrustedIdNoOrganization(String organizationId);

    Mono<List<String>> getTrustedIdNoOrganizationMember(String organizationId, String userId);

    /**
     * Ham lay danh sach don vi hieu luc theo ma to chuc doanh nghiep va ma nguoi
     * dung
     *
     * @param userId
     * @param organizationId
     * @return
     */
    Mono<List<OrganizationUnit>> findAllActiveOrganizationUnitsByOrganizationId(String userId, String organizationId);

    /**
     * Lay so giay to cua doanh nghiep
     *
     * @param idType
     * @param type
     * @param tenantId
     * @return
     */
    Mono<Optional<String>> getIdNoOrganization(String idType, String type, String tenantId);

    Mono<Optional<GetActionLoginReportResponse>> getActionLoginReport(LocalDate dateReport);
}
