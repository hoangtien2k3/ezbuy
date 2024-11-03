package com.ezbuy.productservice.constants;

import java.util.Arrays;

public enum ProductSpecType {
    FILTER_PRICE("FILTER_PRICE", "Lọc theo giá"),
    INVALID("INVALID", "INVALID");
    private final String code;
    private final String name;

    ProductSpecType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String fromKey(String key) {
        ProductSpecType productSpecType = Arrays.stream(ProductSpecType.values())
                .filter(e -> key.equalsIgnoreCase(e.code))
                .findFirst()
                .orElse(INVALID);
        return productSpecType == INVALID ? key : productSpecType.name;
    }
}
