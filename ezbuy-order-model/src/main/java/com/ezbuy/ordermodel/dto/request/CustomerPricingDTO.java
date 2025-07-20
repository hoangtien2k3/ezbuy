package com.ezbuy.ordermodel.dto.request;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
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
