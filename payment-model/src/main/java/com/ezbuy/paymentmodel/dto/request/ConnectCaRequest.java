package com.ezbuy.paymentmodel.dto.request;

import com.ezbuy.paymentmodel.dto.CustomerDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectCaRequest {
    // data tao link checkout payment
    private String cancelUrl;
    private String returnUrl;
    private Double totalFee;
    // thong tin don hang
    private CustomerDTO customerInfo;
    private List<OrderProductDTO> productList;
    private String organizationId; // ma doanh nghiep chon sau khi login
    private String bccsOrderType;
    private String bccsOrderData;
}
