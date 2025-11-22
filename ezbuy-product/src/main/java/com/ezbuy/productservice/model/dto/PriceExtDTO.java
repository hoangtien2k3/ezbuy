package com.ezbuy.productservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceExtDTO {

    @JsonIgnore
    public boolean existsRecord;
    public String key;
    public String productOfferPriceExtId;
    public String productOfferPriceId;
    public String value;
}
