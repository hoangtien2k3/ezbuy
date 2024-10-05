package com.ezbuy.ordermodel.model.response;

import com.ezbuy.ordermodel.model.Characteristic;
import lombok.Data;

@Data
public class CustomerSubscriberSmeInfoDTO {

    private String idNo;
    private String isdn;
    private String telecomServiceId;
    private String telecomServiceAlias; // alias bo sung cho
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
