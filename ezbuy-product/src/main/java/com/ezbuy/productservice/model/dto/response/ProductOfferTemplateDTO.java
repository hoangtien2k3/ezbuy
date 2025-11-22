package com.ezbuy.productservice.model.dto.response;

import com.ezbuy.productservice.model.dto.BaseProductSpecDTO;
import com.ezbuy.productservice.model.dto.PriceTemplateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

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
