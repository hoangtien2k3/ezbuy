package com.ezbuy.ordermodel.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingProductResponse {
    List<PricingProductItemResponse> pricingProductItems;
}
