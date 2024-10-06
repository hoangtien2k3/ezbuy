package com.ezbuy.ordermodel.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrderItem {
    private String subscriberId;
    private Long id;
    private String action;
    private Long quantity;

    @Valid
    private Product product;

    @Valid
    private ProductOfferingRef productOfferingRef;
}
