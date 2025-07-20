package com.ezbuy.productmodel.dto.response;

import com.ezbuy.productmodel.dto.BaseProductSpecDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
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
public class ProductSpecCharDTO extends BaseProductSpecDTO {

    @JsonIgnore
    private int charType;

    @JsonIgnore
    private boolean checkPrice;

    private String code;

    @JsonIgnore
    private Date createDatetime;

    @JsonIgnore
    private String createUser;

    private String dataType;

    @JsonIgnore
    private String extData;

    private String name;
    private String productSpecCharId;
    private String productSpecCharTypeId;
    private int status;

    @JsonIgnore
    private Date updateDatetime;

    @JsonIgnore
    private String updateUser;

    private String valueType;
    private ProductSpecCharValueDTO productSpecCharValueDTO;
    private List<ProductSpecCharValueDTO> productSpecCharValueDTOList;

    @JsonIgnore
    private int maxCardinality;

    @JsonIgnore
    private int minCardinality;

    private String value;
    private List<ProductOfferPriceDTO> lstPriceTemplate;
}
