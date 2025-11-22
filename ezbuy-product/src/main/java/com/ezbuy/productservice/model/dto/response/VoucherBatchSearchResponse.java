package com.ezbuy.productservice.model.dto.response;

import com.ezbuy.productservice.model.entity.VoucherBatch;
import java.util.List;
import lombok.Data;

@Data
public class VoucherBatchSearchResponse {
    private List<VoucherBatch> voucherBatch;
    private PaginationDTO pagination;
}
