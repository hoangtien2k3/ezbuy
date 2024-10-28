package com.ezbuy.ordermodel.dto.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerPricingDTO {
    private Integer groupType;

    @XmlElement(name = "listCustIdentity")
    private CustomerIdentityDTO customerIdentity;
}
