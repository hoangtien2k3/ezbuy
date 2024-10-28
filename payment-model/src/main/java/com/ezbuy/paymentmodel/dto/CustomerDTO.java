package com.ezbuy.paymentmodel.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long custId;
    private String custType;
    private String groupType;
    private String birthDate;
    private String name;
    private String contactEmail;
    private String telMobile;
    private String areaCode;
    private String district;
    private String precinct;
    private String address;
    private String province;
    private List<CustIdentityDTO> listCustIdentity;
    private RepresentativeCustDTO representativeCust;
}
