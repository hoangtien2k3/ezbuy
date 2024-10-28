package com.ezbuy.ordermodel.dto.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class OrderStateDTO {

    @XmlElement(name = "bpId")
    private String bpId;

    @XmlElement(name = "reasonCancel")
    private String reasonCancel;

    @XmlElement(name = "state")
    private Integer state;
}
