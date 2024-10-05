package com.ezbuy.ordermodel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingProductResponse {
    List<PricingProductItemResponse> pricingProductItems;
}
