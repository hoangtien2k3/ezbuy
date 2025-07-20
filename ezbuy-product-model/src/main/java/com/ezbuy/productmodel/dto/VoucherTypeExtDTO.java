package com.ezbuy.productmodel.dto;

import com.ezbuy.productmodel.model.VoucherType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherTypeExtDTO extends VoucherType {
    @JsonIgnore
    private String voucherCode;

    public VoucherTypeExtDTO(VoucherType voucherType) {
        super(voucherType);
    }
}
