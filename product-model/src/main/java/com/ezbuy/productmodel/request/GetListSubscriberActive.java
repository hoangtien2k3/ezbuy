package com.ezbuy.productmodel.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class GetListSubscriberActive {
    @NotEmpty(message = "idNo.required")
    private List<String> lstIdNo;

    @NotEmpty(message = "telecom.service.alias.required")
    private List<String> lstTelecomServiceAlias;
}
