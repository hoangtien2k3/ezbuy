package com.ezbuy.ordermodel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class ProductPaidDTO {

    private Boolean isBundle;

    private List<ProductTemplateCharacteristicDTO> productCharacteristic;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductRelationshipDTO> lstProductRelationship;
}
