package com.ezbuy.productmodel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaginationDTO {
    private Integer pageIndex;
    private Integer pageSize;
    private Long totalRecords;
}
