package com.ezbuy.order.model.response;

import com.ezbuy.order.model.Characteristic;
import lombok.Data;

@Data
public class CustomerSubscriberSmeInfoDTO {
    private String idNo;
    private String isdn;
    private String telecomServiceId;
    private String telecomServiceAlias;
    private String productId;
    private String productCode;
    private String name;
    private String subscriberId;
    private Double price;
    private String id;
    private Characteristic characteristic;
    private Integer orderState;
    private Integer isBundle;
    private String individualId;
}
