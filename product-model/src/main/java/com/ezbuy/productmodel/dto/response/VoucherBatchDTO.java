package com.ezbuy.productmodel.dto.response;

import com.ezbuy.productmodel.model.VoucherBatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherBatchDTO {
  private List<VoucherBatch> vouchers;
  private PaginationDTO pagination;
}
