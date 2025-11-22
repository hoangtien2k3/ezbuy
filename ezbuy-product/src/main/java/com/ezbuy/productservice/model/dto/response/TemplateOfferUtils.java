package com.ezbuy.productservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;

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
