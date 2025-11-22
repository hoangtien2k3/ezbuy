package com.ezbuy.productservice.model.dto;

import com.ezbuy.productservice.model.entity.VoucherType;
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
