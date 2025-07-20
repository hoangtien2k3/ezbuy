package com.ezbuy.ordermodel.dto.request;

import com.ezbuy.ordermodel.model.Characteristic;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCustomerSubscriberSmeInfoRequest {

    List<Characteristic> characteristicList;
    private String idNo;
    private String isdn;
    private String telecomServiceId;
    private String telecomServiceAlias; // bo sung alias PYCXXX/LuongToanTrinhScontract
    private String productId;
    private String productCode;
    private String name;
    private String subscriberId;
    private Double price;
    private String id;
    private Integer isBundle;
    private String organizationId;
    private String individualId;
}
