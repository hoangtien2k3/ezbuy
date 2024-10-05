package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTemplateCharacteristicPaidDTO {

    //    private String code;
    private String desc;
    private String name;
    private String value;
    private String valueType;
}
