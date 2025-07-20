package com.ezbuy.ordermodel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;

@Data
public class ProductPaidDTO {

    private Boolean isBundle;

    private List<ProductTemplateCharacteristicDTO> productCharacteristic;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductRelationshipDTO> lstProductRelationship;
}
