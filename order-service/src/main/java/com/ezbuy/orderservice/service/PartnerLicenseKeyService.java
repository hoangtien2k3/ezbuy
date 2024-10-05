package com.ezbuy.orderservice.service;


import com.ezbuy.sme.ordermodel.dto.PartnerLicenseKeyDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PartnerLicenseKeyService {
    /**
     * Lay thong tin licence key cho danh sach alias
     * @param userId
     * @param organizationId
     * @param lstAliasCreate
     * @return
     */
    Mono<List<PartnerLicenseKeyDTO>> createLicenseKey(String userId, String organizationId, List<PartnerLicenseKeyDTO> lstAliasCreate);
}
