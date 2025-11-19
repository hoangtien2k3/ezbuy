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
public class OrderStateDTO {

    @XmlElement(name = "bpId")
    private String bpId;

    @XmlElement(name = "reasonCancel")
    private String reasonCancel;

    @XmlElement(name = "state")
    private Integer state;
}
