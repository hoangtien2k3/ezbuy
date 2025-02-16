package com.ezbuy.productmodel.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVoucherGiftRequest {
    private List<String> voucherGiftList; // ma voucher tang
    private String username; // user dang nhap
    private String userId; // user id
    private String sourceOrderId; // ma don hang
}
