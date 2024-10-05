package com.ezbuy.productmodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class PriceExtDTO {

    @JsonIgnore
    public boolean existsRecord;
    public String key;
    public String productOfferPriceExtId;
    public String productOfferPriceId;
    public String value;
}
