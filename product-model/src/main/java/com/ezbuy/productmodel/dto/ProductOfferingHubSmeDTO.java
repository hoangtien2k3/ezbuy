package com.ezbuy.productmodel.dto;

import com.ezbuy.productmodel.response.ProductOfferTemplateDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties
public class ProductOfferingHubSmeDTO {
    TelecomServiceDTO telecomServiceDTO;
    List<ProductOfferTemplateDTO> productOfferTemplateDTOS;
}
