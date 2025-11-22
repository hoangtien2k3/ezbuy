package com.ezbuy.productservice.model.dto.response;

import com.ezbuy.productservice.model.entity.Product;
import java.util.List;
import lombok.Data;

@Data
public class GetProductInfoResponse {
    private List<Product> records;
    private List<String> ids;
}
