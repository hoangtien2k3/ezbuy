package com.ezbuy.productmodel.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class FilterGetListSubscriberActive {
    @NotEmpty(message = "idNo.required")
    private String idNo;

    @NotEmpty(message = "telecom.service.id.required")
    private List<String> lstTelecomServiceId;
}
