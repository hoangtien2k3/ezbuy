package com.ezbuy.ordermodel.dto.ws;

import com.ezbuy.ordermodel.dto.CustomerPaidDTO;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

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
