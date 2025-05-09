package com.ezbuy.productmodel.dto.response;

import com.ezbuy.productmodel.model.Product;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchResult {
    private int total; // tong so ban ghi
    private int pageIndex; // so trang
    private int pageSize; // kich thuoc trang
    private List<Product> dataList; // danh sach ban ghi product
}
