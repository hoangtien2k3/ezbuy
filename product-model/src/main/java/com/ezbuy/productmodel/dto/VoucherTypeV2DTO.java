package com.ezbuy.productmodel.dto;

import com.ezbuy.productmodel.model.VoucherType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherTypeV2DTO extends VoucherType {
    private String voucherCode; // ma voucher
}
