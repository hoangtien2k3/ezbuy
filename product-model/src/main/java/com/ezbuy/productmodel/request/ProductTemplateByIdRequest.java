package com.ezbuy.productmodel.request;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@Data
public class ProductTemplateByIdRequest {

    @XmlElement(name = "lstTemplateIds")
    private List<String> templateIds;
}
