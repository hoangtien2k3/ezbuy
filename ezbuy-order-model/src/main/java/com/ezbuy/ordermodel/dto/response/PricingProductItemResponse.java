package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.ordermodel.dto.request.ItemPriceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingProductItemResponse {
    private ItemPriceDTO itemPrice;
}
