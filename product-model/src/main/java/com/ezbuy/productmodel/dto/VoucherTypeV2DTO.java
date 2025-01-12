package com.ezbuy.productmodel.dto;

import com.ezbuy.productmodel.model.VoucherType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherTypeV2DTO extends VoucherType {
    private String voucherCode; // ma voucher

    public VoucherTypeV2DTO(VoucherType voucherType) {
        super(voucherType);
    }
}
