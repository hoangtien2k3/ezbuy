package com.ezbuy.productmodel.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class FilterGetListSubscriberActive {
    @NotEmpty(message = "idNo.required")
    private String idNo;
    @NotEmpty(message = "telecom.service.id.required")
    private List<String> lstTelecomServiceId;
}
