package com.ezbuy.productmodel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationCAResponse {
    private String productId;
    private List<PriceProductDTO> productPrices;
}
