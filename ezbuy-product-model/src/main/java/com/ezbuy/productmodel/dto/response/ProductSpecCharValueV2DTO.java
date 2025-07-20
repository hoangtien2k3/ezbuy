package com.ezbuy.productmodel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductSpecCharValueV2DTO {
    private String name;
    private String value;
    private ProductOfferPriceV2DTO productOfferPriceDTO;
}
