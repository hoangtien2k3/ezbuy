package com.ezbuy.productservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseProductSpecDTO {

    @JsonIgnore
    public boolean success;

    @JsonIgnore
    public boolean checkImport;

    @JsonIgnore
    public boolean expandRow;

    @JsonIgnore
    public boolean expandRowDetail;

    @JsonIgnore
    public boolean expandRowProduct;
}
