package com.ezbuy.ordermodel.dto.ws;

import com.ezbuy.ordermodel.dto.request.ProductOrderItemDTO;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class PricingProductWSResponse {

    private List<ProductOrderItemDTO> productOrderItem;
}
