package com.ezbuy.ordermodel.dto.ws;

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

@XmlRootElement(name = "return")
public class GetCustomerSubscriberSmeInfoResponse {

    @XmlElement(name = "code")
    private String code;

    @XmlElement(name = "lstCustomerDTO")
    private LstCustomerDTO lstCustomerDTO;
}
