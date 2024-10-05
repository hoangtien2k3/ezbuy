package com.ezbuy.ordermodel.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class PlaceOrderResponse {

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "errorCode")
    private String errorCode;

    @XmlElement(name = "success")
    private String success;

}
