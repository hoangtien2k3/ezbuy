package com.ezbuy.ordermodel.dto.ws;

import com.ezbuy.ordermodel.dto.CustomerPaidDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "lstCustomerDTO")
public class LstCustomerDTO {

    private Long custId;
    private String custType;

    @XmlElement(name = "custTypeDTO")
    private CustTypeDTO custTypeDTO;

    private String name;
    private String birthDate;
    private String areaCode;
    private String province;
    private String district;
    private String precinct;
    private String address;
    private String telMobile;
    private String contactEmail;

    private List<CustIdentity> listCustIdentity;
    private CustomerPaidDTO representativeCust;
    private ListSubscriberDTO listSubscriber;
}
