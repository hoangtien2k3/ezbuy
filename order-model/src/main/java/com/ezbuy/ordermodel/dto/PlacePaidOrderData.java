package com.ezbuy.ordermodel.dto;

import com.ezbuy.ordermodel.dto.ws.ProductOrderItemWsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacePaidOrderData {

    private CustomerPaidDTO customer;

    private List<ServiceItemPaidDTO> serviceItem;

    private String systemType;

    private String systemStaffCode;

    private String staffRevenue;

    private String transactionPlace;

    private String recipientPhone;

    private String recipientName;

    private String target;

    private List<ProductOrderItemWsDTO> productOrderItem;

    private Double totalFee;

    private PayInfoPaidOrderDTO payInfo;

    private Long payStatus;

    private Boolean needSignCAContract;

    private Boolean needAssignStaff;

    private Boolean needFillInfo;

    private String businessCode;

    private Boolean custAcceptAIResult;

    private Boolean checkByAI;

    private List<OrderFileDTO> customerFile;

    private List<ProductTemplateDTO> lstProductTemplate;

    private Boolean selfCare;

    private InvoiceInfoDTO invoiceInfo; //thong tin xuat hoa don

    private String extCode; //Thong tin orderId cua HUB gui sang bccs_order de doi xoat

    private Boolean revenueForAm; //bien check len cong no

    private Boolean needCompleteProfile; // flag thuc hien luong xac minh CA khi tao don order
}
