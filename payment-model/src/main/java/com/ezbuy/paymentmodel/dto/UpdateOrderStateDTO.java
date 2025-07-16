package com.ezbuy.paymentmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStateDTO {
    private String orderCode;
    private String orderType;
    private Integer orderState;
}
