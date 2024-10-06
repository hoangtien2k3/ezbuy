package com.ezbuy.paymentmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPaymentRequest {

    private String orderCode;
    private String cancelUrl;
    private String returnUrl;
    private Long totalFee;
    private String orderType;
    private String telecomServiceAlias; //alias theo dich vu
    private List<PaymentOrderDetailDTO> lstPaymentOrderDetail;
}
