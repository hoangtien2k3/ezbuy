package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTemplateCharacteristicDTO {

    private String desc;
    private String name;
    private String value;
    private String valueType;
    private Long quantity;
}
