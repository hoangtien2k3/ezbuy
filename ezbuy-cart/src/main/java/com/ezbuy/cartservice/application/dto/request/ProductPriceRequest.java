package com.ezbuy.cartservice.application.dto.request;

import com.ezbuy.cartservice.application.dto.ProductItem;
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
