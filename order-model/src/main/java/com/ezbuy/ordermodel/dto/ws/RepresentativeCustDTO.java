package com.ezbuy.ordermodel.dto.ws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "representativeCust")
public class RepresentativeCustDTO {

    private Long custId;
    private String custType;

    // @XmlElement(name = "custTypeDTO")
    // private CustTypeDTO custTypeDTO;

    private String name;
    private String birthDate;
    private String areaCode;
    private String province;
    private String district;
    private String precinct;
    private String address;
    private List<CustIdentity> listCustIdentity;
}
