package com.ezbuy.productmodel.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class CustomerSubscribeInfoCMResponse {

    @XmlElement(name = "custTypeDTO")
    private CustTypeCMResponse custTypeDTO;

    @XmlElement(name = "listSubscriber")
    private List<SubscriberCMResponse> listSubscriber;
}