package com.ezbuy.productmodel.response;

import com.ezbuy.productmodel.model.Product;
import lombok.Data;

import java.util.List;

@Data
public class GetProductInfoResponse {
    private List<Product> records;
    private List<String> ids;
}
