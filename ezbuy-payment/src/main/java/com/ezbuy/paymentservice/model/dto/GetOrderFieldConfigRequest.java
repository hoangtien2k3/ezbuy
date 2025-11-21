package com.ezbuy.paymentservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOrderFieldConfigRequest {
    private List<String> lstTelecomServiceAlias;
    private String orderType;
}
