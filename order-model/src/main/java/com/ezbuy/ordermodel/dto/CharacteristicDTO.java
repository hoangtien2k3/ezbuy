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
public class CharacteristicDTO {

    @Length(max = 50, message = "order.items.productCharacteristic.name.over.length")
    private String name;

    @Length(max = 20, message = "order.items.productCharacteristic.valueType.over.length")
    private String valueType;

    @Length(max = 50, message = "order.items.productCharacteristic.value.over.length")
    private String value;

    @Length(max = 50, message = "order.items.productCharacteristic.code.over.length")
    private String code;

    private Long quantity;

    public CharacteristicDTO(
            @Length(max = 50, message = "order.items.productCharacteristic.name.over.length") String name,
            @Length(max = 20, message = "order.items.productCharacteristic.valueType.over.length") String valueType,
            @Length(max = 50, message = "order.items.productCharacteristic.value.over.length") String value,
            @Length(max = 50, message = "order.items.productCharacteristic.code.over.length") String code) {
        this.name = name;
        this.valueType = valueType;
        this.value = value;
        this.code = code;
    }
}
