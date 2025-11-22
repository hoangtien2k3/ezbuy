package com.ezbuy.productservice.model.dto;

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
public class ProductOfferingDTO {
    String code;
    String name;
    String productOfferingId;
    List<TelecomServiceDTO> lstTelecomServiceDTO;
    List<ProductOfferExt> productOfferExts;
}
