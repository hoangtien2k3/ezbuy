package com.ezbuy.productmodel.response;

import com.ezbuy.productmodel.dto.ProductOfferingHubSmeDTO;
import java.util.List;
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
public class GetListTemplateComboForHubSmeResponse {
    @XmlElement(name = "listTemplateCombo")
    private List<ProductOfferingHubSmeDTO> listTemplateCombo;
}
