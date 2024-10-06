package com.ezbuy.productmodel.response;


import com.ezbuy.productmodel.constants.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ezbuy.productmodel.dto.BaseProductSpecDTO;
import com.ezbuy.productmodel.dto.PriceTemplateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;
import java.util.Optional;

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
    private String telecomServiceAlias; // bo sung them alias PYCXXX/LuongToanTrinhScontract
    private String templateCode;
    private String templateName;
    private Long totalPrice;
    private String serviceAlias; //alias do product tra them 04/04

    private String durationValue;

    public void handleDataGetByIds() {
        if (lstSpecTemplate == null || lstSpecTemplate.isEmpty()) {
            return;
        }
        if(Constants.ServiceId.VBHXH.equals(telecomServiceId) || Constants.TelecomServiceAlias.VBHXH.equals(telecomServiceAlias)){
            Optional<ProductSpecCharDTO> productSpecCharOptional = lstSpecTemplate.stream()
                    .filter(prod -> "MONTH_USE".equals(prod.getCode()))
                    .findFirst();
            productSpecCharOptional.ifPresent(productSpecCharDTO -> this.durationValue = productSpecCharDTO.getValue());
        }
        Optional<ProductSpecCharDTO> productSpecCharOptional = lstSpecTemplate.stream()
                .filter(prod -> "PREPAID_DURATION".equals(prod.getCode()))
                .findFirst();
        productSpecCharOptional.ifPresent(productSpecCharDTO -> this.durationValue = productSpecCharDTO.getValue());
    }
}
