package com.ezbuy.paymentmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RequestBankingSyncDTO {

    private String vtTransactionId;
    private Integer paymentStatus;
    private String id;
    private String orderCode;
    private Integer updateState;
}
