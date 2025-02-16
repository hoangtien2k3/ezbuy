package com.ezbuy.productmodel.dto.response;

import com.ezbuy.productmodel.dto.VoucherTypeDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchVoucherTypeResponse {
    private List<VoucherTypeDTO> listVoucherType; // danh sach loai khuyen mai
    private PaginationDTO pagination; // thong tin phan trang
}
