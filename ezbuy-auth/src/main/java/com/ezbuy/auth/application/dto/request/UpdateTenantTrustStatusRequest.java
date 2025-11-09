package com.ezbuy.auth.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTenantTrustStatusRequest {
    private String tenantId;
    private Integer trustStatus;
}
