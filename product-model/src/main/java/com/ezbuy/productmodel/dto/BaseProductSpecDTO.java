package com.ezbuy.productmodel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@XmlAccessorType(XmlAccessType.FIELD)
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
