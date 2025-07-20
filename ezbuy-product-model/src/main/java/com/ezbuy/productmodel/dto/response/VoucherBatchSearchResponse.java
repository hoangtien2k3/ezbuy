package com.ezbuy.productmodel.dto.response;

import com.ezbuy.productmodel.model.VoucherBatch;
import java.util.List;
import lombok.Data;

@Data
public class VoucherBatchSearchResponse {
    private List<VoucherBatch> voucherBatch;
    private PaginationDTO pagination;
}
