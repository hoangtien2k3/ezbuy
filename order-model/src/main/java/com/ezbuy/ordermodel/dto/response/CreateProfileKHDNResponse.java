package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.ordermodel.dto.ProfileForBusinessCustDTO;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "return")
@XmlAccessorType(XmlAccessType.PROPERTY)
@Data
public class CreateProfileKHDNResponse {
    private String code;
    private String description;
    private List<ProfileForBusinessCustDTO> lstProfileForBusinessCust;
    private List<ProfileForBusinessCustDTO> profileForBusinessCust;
    private String faultcode;
    private String faultstring;
}
