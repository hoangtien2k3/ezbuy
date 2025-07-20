package com.ezbuy.productmodel.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchProductRequest {
    private String name;
    private String code;
    private String unit;
    private Integer lockStatus;
    private Integer pageIndex;
    private Integer pageSize;
    private List<String> sort;
}
