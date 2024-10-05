package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerLicenseKeyDTO {
    private String serviceAlias;
    private Long telecomServiceId;
    private String licenceKey;

}
