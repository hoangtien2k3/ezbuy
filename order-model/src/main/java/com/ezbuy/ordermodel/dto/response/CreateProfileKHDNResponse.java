package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.ordermodel.dto.ProfileForBusinessCustDTO;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

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
