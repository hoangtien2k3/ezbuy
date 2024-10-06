package com.ezbuy.productmodel.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class GetListSubscriberActive {
    @NotEmpty(message = "idNo.required")
    private List<String> lstIdNo;
    @NotEmpty(message = "telecom.service.alias.required")
    private List<String> lstTelecomServiceAlias;
}