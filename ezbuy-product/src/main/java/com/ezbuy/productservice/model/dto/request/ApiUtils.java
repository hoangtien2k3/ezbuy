package com.ezbuy.productservice.model.dto.request;

import com.ezbuy.productservice.constants.enumeration.SortOrder;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ApiUtils {
    private SortOrder sortOrder;
    private String sortField;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer pageCount;
    private List<FilterRequest> listFilter;
}
