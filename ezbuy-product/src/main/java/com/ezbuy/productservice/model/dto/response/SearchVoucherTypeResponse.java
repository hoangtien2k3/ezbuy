package com.ezbuy.productservice.model.dto.response;

import com.ezbuy.productservice.model.dto.VoucherTypeDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchVoucherTypeResponse {
    private List<VoucherTypeDTO> listVoucherType;
    private PaginationDTO pagination;
}
