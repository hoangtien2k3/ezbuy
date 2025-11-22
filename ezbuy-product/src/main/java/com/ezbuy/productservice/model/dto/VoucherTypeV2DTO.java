package com.ezbuy.productservice.model.dto;

import com.ezbuy.productservice.model.entity.VoucherType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherTypeV2DTO extends VoucherType {
    private String voucherCode; // ma voucher
}
