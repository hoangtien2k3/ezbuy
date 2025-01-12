package com.ezbuy.productmodel.dto.response;

import com.ezbuy.productmodel.model.Voucher;
import lombok.Data;

import java.util.List;

@Data
public class VoucherSearchResponse {
  private List<Voucher> voucher;
  private PaginationDTO pagination;
}
