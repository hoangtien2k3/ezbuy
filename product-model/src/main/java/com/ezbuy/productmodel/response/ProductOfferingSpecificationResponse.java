package com.ezbuy.productmodel.response;

import com.ezbuy.productmodel.dto.BaseProductSpecDTO;
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
public class ProductOfferingSpecificationResponse extends BaseProductSpecDTO {

    @XmlElement(name = "lstProductSpecCharUseDTO")
    private List<ProductSpecCharUseDTO> lstProductSpecCharUseDTO;
}
