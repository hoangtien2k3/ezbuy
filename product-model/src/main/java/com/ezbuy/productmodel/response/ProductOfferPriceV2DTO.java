package com.ezbuy.productmodel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOfferPriceV2DTO {
    private Double price;
    private List<PriceExtDTO> lstPriceExt;
}
