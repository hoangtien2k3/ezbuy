package com.ezbuy.productmodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@XmlAccessorType(XmlAccessType.FIELD)
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
