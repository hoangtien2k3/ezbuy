package com.ezbuy.productmodel.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class TemplateOfferUtils {
    @JsonIgnore

    private List<Object> listFilter;

    private Integer pageCount;
    private Integer pageIndex;
    private Integer pageSize;
    private String sortField;
    private String sortOrder;

}
