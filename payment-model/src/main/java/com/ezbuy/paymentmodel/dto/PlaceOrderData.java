package com.ezbuy.paymentmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceOrderData {
    private ServiceItemDTO serviceItem;
    private ListProductTemplateDTO lstProductTemplate;
    private CustomerDTO customer;
    private Long totalFee;
    private String systemType;
    private String transactionPlace;
    private String recipientName;
    private Boolean needFillInfo;
    private Boolean needAssignStaff;
    private Boolean needSignCAContract;
}
