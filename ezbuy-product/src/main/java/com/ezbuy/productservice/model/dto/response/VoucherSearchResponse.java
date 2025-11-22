package com.ezbuy.productservice.model.dto.response;

import com.ezbuy.productservice.model.entity.Voucher;
import java.util.List;
import lombok.Data;

@Data
public class VoucherSearchResponse {
    private List<Voucher> voucher;
    private PaginationDTO pagination;
}
