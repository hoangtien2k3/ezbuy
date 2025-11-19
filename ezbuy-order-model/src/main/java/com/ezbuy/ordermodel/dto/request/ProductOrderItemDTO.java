package com.ezbuy.ordermodel.dto.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductOrderItemDTO {

    private ProductDTO product;
    private ProductOfferingRefRequest productOffering;
    private Integer quantity = 1;
    private ItemPriceDTO itemPrice;
}
