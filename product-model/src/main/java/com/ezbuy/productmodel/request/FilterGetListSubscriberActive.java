package com.ezbuy.productmodel.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class FilterGetListSubscriberActive {
    @NotEmpty(message = "idNo.required")
    private String idNo;
    @NotEmpty(message = "telecom.service.id.required")
    private List<String> lstTelecomServiceId;
}
