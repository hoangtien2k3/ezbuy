package com.ezbuy.productservice.model.dto.response;

import com.ezbuy.productservice.model.dto.BaseProductSpecDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductSpecCharValueDTO extends BaseProductSpecDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Date createDatetime;

    @JsonIgnore
    private String createUser;

    @JsonIgnore
    private boolean def;

    @JsonIgnore
    private int isDefault;

    private String code;

    private String name;
    private String productSpecCharId;
    private String productSpecCharValueId;
    private int status;

    @JsonIgnore
    private Date updateDatetime;

    @JsonIgnore
    private String updateUser;

    private String value;
    private String valueType;

    @JsonIgnore
    public boolean isSuccess() {
        return success;
    }
}
