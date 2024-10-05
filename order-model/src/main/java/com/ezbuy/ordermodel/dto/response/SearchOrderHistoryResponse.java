package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.ordermodel.dto.OrderDetailDTO;
import com.ezbuy.ordermodel.dto.PaginationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchOrderHistoryResponse {

    private List<OrderDetailDTO> data;

    private PaginationDTO pagination;

    public SearchOrderHistoryResponse(int pageIndex, int pageSize) {
        data = new ArrayList<>();
        pagination = PaginationDTO.builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
    }
}
