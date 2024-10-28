package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductDTO {

    private String templateId;

    private String templateCode;

    private String telecomServiceId;

    private String telecomServiceAlias; // alias cua dich vu (CA, VCONTRACT) PYCXXX/LuongToanTrinhScontract

    private Integer quantity;
}
