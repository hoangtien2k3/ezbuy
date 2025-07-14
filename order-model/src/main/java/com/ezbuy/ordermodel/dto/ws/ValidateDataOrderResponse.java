package com.ezbuy.ordermodel.dto.ws;

import com.ezbuy.ordermodel.dto.ErrorDTO;
import java.util.List;
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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class ValidateDataOrderResponse {

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "errorCode")
    private String errorCode;

    @XmlElement(name = "success")
    private String success;

    @XmlElement(name = "listError")
    private List<ErrorDTO> listError;
}
