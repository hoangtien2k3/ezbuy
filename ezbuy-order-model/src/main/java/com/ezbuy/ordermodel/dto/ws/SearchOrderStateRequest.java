package com.ezbuy.ordermodel.dto.ws;

import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@Data
public class SearchOrderStateRequest {

    @XmlElement(name = "lstBpId")
    private List<String> orderCodeList;
}
