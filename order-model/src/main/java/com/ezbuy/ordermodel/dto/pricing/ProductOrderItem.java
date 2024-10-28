package com.ezbuy.ordermodel.dto.pricing;

import com.ezbuy.ordermodel.dto.Product;
import com.ezbuy.ordermodel.dto.ProductOfferingRef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductOrderItem {
    private String id;
    private Long quantity;
    private String action;
    private List<OrderPrice> itemPrice;
    private OrderPrice totalPrice;
    private Long subscriberId;
    private Long thirdPartySubscriberId;
    private String businessCode;
    private String staffCode;
    private Long isPostpaid;
    private Long priceDailyMain;
    private ProductOfferingRef productOffering;
    private Product product;
    private String telecomServiceName; // ten dich vu dau noi
}
