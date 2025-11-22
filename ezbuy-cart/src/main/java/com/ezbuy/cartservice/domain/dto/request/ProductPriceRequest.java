package com.ezbuy.cartservice.domain.dto.request;

import com.ezbuy.cartservice.domain.dto.ProductItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceRequest {
    List<ProductItem> productItems;
}
