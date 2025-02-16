package com.ezbuy.productmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVoucherTransactionRequest {
    private String voucherId; // id bang voucher
    private String userId; // id user dang nhap
    private String state; // trang thai
    private String sourceOrderId; // id bang sme_order.order
    private String transactionType; // CONNECT - dau noi moi, AFTER - sau ban
    private Integer amount; // so tien duoc giam
}
