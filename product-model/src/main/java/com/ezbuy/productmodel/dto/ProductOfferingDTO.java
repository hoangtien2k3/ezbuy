package com.ezbuy.productmodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties
public class ProductOfferingDTO {
    String code; //ma goi combo
    String name; //ten goi combo
    String productOfferingId; //id goi combo
    List<TelecomServiceDTO> lstTelecomServiceDTO; //danh sach goi cuoc thuoc combo
    List<ProductOfferExt> productOfferExts; //danh sach khuyen mai + gia
}
