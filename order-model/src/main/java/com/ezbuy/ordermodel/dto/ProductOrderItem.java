package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

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
