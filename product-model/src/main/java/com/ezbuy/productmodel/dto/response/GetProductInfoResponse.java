package com.ezbuy.productmodel.dto.response;

import com.ezbuy.productmodel.model.Product;
import java.util.List;
import lombok.Data;

@Data
public class GetProductInfoResponse {
    private List<Product> records;
    private List<String> ids;
}
