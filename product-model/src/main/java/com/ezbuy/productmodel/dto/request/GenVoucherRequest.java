package com.ezbuy.productmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenVoucherRequest {
    private String voucherTypeId;
    private String voucherBatchId;
}
