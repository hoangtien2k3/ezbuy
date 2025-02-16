package com.ezbuy.productmodel.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVoucherUseRequest {
    private String voucherId; // id bang voucher
    private String userId; // id user dang nhap
    private String usename; // user dang nhap
    private String systemType; // he thong ap dung voucher
    private LocalDateTime expiredDate; // ngay het han
    private String state; // trang thai
    private String sourceOrderId; // id bang sme_order.order
}
