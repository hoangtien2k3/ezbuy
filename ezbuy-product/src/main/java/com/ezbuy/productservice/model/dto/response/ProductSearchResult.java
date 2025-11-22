package com.ezbuy.productservice.model.dto.response;

import com.ezbuy.productservice.model.entity.Product;
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
    private int total;
    private int pageIndex;
    private int pageSize;
    private List<Product> dataList;
}
