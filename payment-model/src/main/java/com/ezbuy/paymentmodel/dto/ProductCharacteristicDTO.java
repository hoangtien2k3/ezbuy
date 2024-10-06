package com.ezbuy.paymentmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCharacteristicDTO {
    private String code;
    private String name;
    private String value;
    private String valueType;
}
