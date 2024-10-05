package com.ezbuy.ordermodel.dto.request;

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

    private List<String> lstTelecomServiceAlias; // thay telecomServiceId thanh alias

    private String orderType;

}
