package com.ezbuy.ordermodel.dto;

import lombok.Data;

@Data
public class PayInfoPaidOrderDTO extends PayInfoDTO {
    private String payMethod;
    private String transactionIdNo;
}
