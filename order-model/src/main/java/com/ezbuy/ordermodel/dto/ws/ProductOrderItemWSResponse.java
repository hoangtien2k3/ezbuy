package com.ezbuy.ordermodel.dto.ws;

import com.ezbuy.ordermodel.dto.request.ItemPriceDTO;
import com.ezbuy.ordermodel.dto.request.ProductOfferingRefRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

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
