package com.ezbuy.cartservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItem {
    private String templateId;
    private Long price;
    private Integer quantity;
}
