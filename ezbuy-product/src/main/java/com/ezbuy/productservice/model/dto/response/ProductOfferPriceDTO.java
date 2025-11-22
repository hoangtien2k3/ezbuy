package com.ezbuy.productservice.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOfferPriceDTO {
    private Long productOfferPriceId;
    private Long priceTypeId;
    private String priceTypeCode;
    private Long price;
    private Long vat;
}
