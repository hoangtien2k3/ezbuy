package com.ezbuy.productmodel.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class FilterGetListSubscriberActiveByAlias {
    @NotEmpty(message = "idNo.required")
    private String idNo;
    @NotEmpty(message = "telecom.service.alias.required")
    private List<String> lstTelecomServiceAlias;
}
