package com.ezbuy.ordermodel.dto;

import com.ezbuy.ordermodel.dto.ws.CustIdentity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPaidDTO extends CustomerDTO {

    private Long custId;

    private String custType;

    private String groupType;

    private String birthDate;

    private List<CustIdentity> listCustIdentity;

    private CustomerPaidDTO representativeCust;

    private CustomerPaidDTO receiverCust;

    private String systemType;

    private String contactTitle; //chuc vu

    private AddressDTO addressDTO; //thong tin dia chi

    private Long issueCompany; //ngay thanh lap doanh nghiep lay tu thong tin bang tenant_identify

    private CustomerPaidDTO userInfoDTO; //thong tin nguoi dai dien

}
