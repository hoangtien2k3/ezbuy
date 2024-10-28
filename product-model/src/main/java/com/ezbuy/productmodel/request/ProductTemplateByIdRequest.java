package com.ezbuy.productmodel.request;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@Data
public class ProductTemplateByIdRequest {

    @XmlElement(name = "lstTemplateIds")
    private List<String> templateIds;
}
