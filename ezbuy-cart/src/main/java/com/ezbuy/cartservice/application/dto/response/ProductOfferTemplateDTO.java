package com.ezbuy.cartservice.application.dto.response;

import com.ezbuy.cartservice.application.dto.BaseProductSpecDTO;
import com.ezbuy.cartservice.application.dto.PriceTemplateDTO;
import com.ezbuy.cartservice.application.dto.ProductSpecCharDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties
public class ProductOfferTemplateDTO extends BaseProductSpecDTO {
    private Long cost;
    private String imageLink;
    private List<PriceTemplateDTO> lstPriceTemplate;
    private List<ProductSpecCharDTO> lstSpecLeft;
    private List<ProductSpecCharDTO> lstSpecMainOffer;
    private List<ProductSpecCharDTO> lstSpecTemplate;
    private String productCode;
    private String productName;
    private String productOfferTemplateId;
    private String productOfferingId;
    private String telecomServiceId;
    private String telecomServiceAlias;
    private String templateCode;
    private String templateName;
    private Long totalPrice;
    private String serviceAlias;
    private String durationValue;
}
