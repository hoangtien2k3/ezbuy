package com.ezbuy.productmodel.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class FilterGetListSubscriberActiveByAlias {
    @NotEmpty(message = "idNo.required")
    private String idNo;

    @NotEmpty(message = "telecom.service.alias.required")
    private List<String> lstTelecomServiceAlias;
}
