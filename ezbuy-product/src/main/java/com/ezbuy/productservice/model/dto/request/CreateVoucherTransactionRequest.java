package com.ezbuy.productservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVoucherTransactionRequest {
    private String voucherId;
    private String userId;
    private String state;
    private String sourceOrderId;
    private String transactionType;
    private Integer amount;
}
