package com.ezbuy.cartservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class PriceTemplateDTO extends BaseProductSpecDTO {
    @JsonIgnore
    private boolean booleanLimited;

    @JsonIgnore
    private Date createDatetime;

    @JsonIgnore
    private String createUser;

    @JsonIgnore
    private Date effectDatetime;

    private List<PriceExtDTO> lstPriceExt;
    private Long price;
    private String pricePolicyId;
    private String priceTypeCode;
    private String priceTypeId;
    private String productOfferPriceId;
    private String productOfferingId;
    private int status;

    @JsonIgnore
    private Date updateDatetime;

    @JsonIgnore
    private String updateUser;

    private Long vat;
}
