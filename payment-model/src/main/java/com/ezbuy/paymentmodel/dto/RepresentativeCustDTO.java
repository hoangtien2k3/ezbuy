package com.ezbuy.paymentmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "representativeCust")
public class RepresentativeCustDTO {

    private Long custId;
    private String custType;
    private String name;
    private String birthDate;
    private String sex;
    private String areaCode;
    private String province;
    private String district;
    private String precinct;
    private String address;
    private List<CustIdentityDTO> listCustIdentity;
}
