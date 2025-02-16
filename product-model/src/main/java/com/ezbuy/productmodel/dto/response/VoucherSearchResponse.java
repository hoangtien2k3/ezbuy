package com.ezbuy.productmodel.dto.response;

import com.ezbuy.productmodel.model.Voucher;
import java.util.List;
import lombok.Data;

@Data
public class VoucherSearchResponse {
    private List<Voucher> voucher;
    private PaginationDTO pagination;
}
