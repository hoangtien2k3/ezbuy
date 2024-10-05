package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private Boolean isBundle;


    @Valid
    private List<CharacteristicDTO> productCharacteristic;

    private List<ProductRelationshipDTO> lstProductRelationship;
}
