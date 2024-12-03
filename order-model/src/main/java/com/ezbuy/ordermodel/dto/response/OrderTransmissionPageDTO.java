package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.ordermodel.dto.PaginationDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderTransmissionPageDTO {
    private List<OrderTransmissionDTO> results;
    private PaginationDTO pagination;
}
