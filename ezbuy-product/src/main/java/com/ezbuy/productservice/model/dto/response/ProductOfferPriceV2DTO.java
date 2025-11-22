package com.ezbuy.productservice.model.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOfferPriceV2DTO {
    private Double price;
    private List<PriceExtDTO> lstPriceExt;
}
