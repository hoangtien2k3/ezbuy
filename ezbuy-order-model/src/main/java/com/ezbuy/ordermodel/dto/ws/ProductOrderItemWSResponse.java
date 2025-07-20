package com.ezbuy.ordermodel.dto.ws;

import com.ezbuy.ordermodel.dto.request.ItemPriceDTO;
import com.ezbuy.ordermodel.dto.request.ProductOfferingRefRequest;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductOrderItemWSResponse {
    private List<ProductWSResponse> product;
    private ProductOfferingRefRequest productOffering;
    private Integer quantity = 1;
    private ItemPriceDTO itemPrice;
}
