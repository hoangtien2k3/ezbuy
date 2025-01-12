package com.ezbuy.productmodel.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationCAResponse {
    private String productId;
    private List<PriceProductDTO> productPrices;
}
