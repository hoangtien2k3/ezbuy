package com.ezbuy.productmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetServiceConnectDTO {
    private String organizationId; // id cong ty
    private String serviceId; // id dich vu
    private String telecomServiceAlias; // PYCXXXLuongToanTrinhScontract
}
