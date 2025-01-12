package com.ezbuy.productmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateVoucherGiftRequest {
    private String orderId; //Id bang order
    private Integer orderState; //trang thai ban ghi order: 3: thanh cong, 4: that bai
}
