package com.ezbuy.settingservice.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PagePolicyRequest {
    @NotEmpty(message = "service.id.invalid")
    private String serviceId;

    @NotEmpty(message = "policyCode.id.invalid")
    private String policyCode;
}
