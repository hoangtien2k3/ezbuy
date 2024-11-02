package com.ezbuy.orderservice.service;

import com.ezbuy.ordermodel.dto.PartnerLicenseKeyDTO;
import java.util.List;
import reactor.core.publisher.Mono;

public interface PartnerLicenseKeyService {
    /**
     * Lay thong tin licence key cho danh sach alias
     *
     * @param userId
     * @param organizationId
     * @param lstAliasCreate
     * @return
     */
    Mono<List<PartnerLicenseKeyDTO>> createLicenseKey(
            String userId, String organizationId, List<PartnerLicenseKeyDTO> lstAliasCreate);
}
