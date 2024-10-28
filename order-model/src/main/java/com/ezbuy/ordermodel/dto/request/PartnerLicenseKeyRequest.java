package com.ezbuy.ordermodel.dto.request;

import lombok.Data;

@Data
public class PartnerLicenseKeyRequest {
    private String serviceAlias;
    private String userId; // id user dang nhap
    private String organizationId; // id doanh nghiep
}
