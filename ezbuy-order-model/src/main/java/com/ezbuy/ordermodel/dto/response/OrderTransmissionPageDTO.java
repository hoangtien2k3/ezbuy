package com.ezbuy.ordermodel.dto.response;

import com.ezbuy.ordermodel.dto.PaginationDTO;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderTransmissionPageDTO {
    private List<OrderTransmissionDTO> results;
    private PaginationDTO pagination;
}
