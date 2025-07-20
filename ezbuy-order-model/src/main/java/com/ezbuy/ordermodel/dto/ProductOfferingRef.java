package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOfferingRef {

    private Long id;

    @Length(max = 255, message = "order.items.productOfferingRef.name.over.length")
    private String name;

    private Long telecomServiceId;
    private String telecomServiceAlias; // bo sung theo PYCXXX/LuongToanTrinhScontract

    @Length(max = 50, message = "order.items.productOfferingRef.code.over.length")
    private String code;

    private Long accountId;
}
