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
    private List<String> voucherGiftList;
    private String username;
    private String userId;
    private String sourceOrderId;
}
