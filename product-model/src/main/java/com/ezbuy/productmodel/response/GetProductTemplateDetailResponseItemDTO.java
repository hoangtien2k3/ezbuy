package com.ezbuy.productmodel.response;

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
public class GetProductTemplateDetailResponseItemDTO {
        private String productCode;
        private String productName;
        private String telecomServiceId;
        private String templateCode;
        @XmlElement(name = "lstBundle")
        private List<BundleDTO> lstBundle;
        @XmlElement(name = "lstSpecTemplate")
        private List<ProductTemplateSpecCharDTO> lstSpecTemplate;
        private Boolean success;
        @XmlElement(name = "lstSpecMainOffer")
        private List<ProductSpecCharDTO> lstSpecMainOffer;
        private String productOfferingId;
        @XmlElement(name = "lstSpecLeft")
        private List<ProductSpecCharDTO> lstSpecLeft;
}
