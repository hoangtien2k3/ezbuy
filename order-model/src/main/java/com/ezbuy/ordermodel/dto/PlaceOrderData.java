package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceOrderData {

    private String orderType; // SELFCARE_CONNECT_CA

    private CustomerDTO customer; // thông tin doanh nghiệp

    private String recipientName; //fix cứng

    private String systemType; //fix cứng

    private String target; // không có thông tin

    private Boolean preOrder; // không có thông tin

    private Boolean needAssignStaff; //fix cứng

    private Boolean needSignCAContract; //fix cứng

    private Boolean needFillInfo; //fix cứng

    private Boolean needAutoFillInfo; // không có thông tin

    private String transactionPlace; //fix cứng

    private List<ServiceItemDTO> serviceItem; // không truyền

    private String systemStaffCode;//user he thong

    private List<ProductTemplateDTO> lstProductTemplate; // có truyền: templateCode, thuộc tính mở rộng productCharacteristic

    private String assignStaffCode; //am ho tro truyen sang order

    private List<ProductOrderItem> productOrderItem;

    private PayInfoDTO payInfo;

}
