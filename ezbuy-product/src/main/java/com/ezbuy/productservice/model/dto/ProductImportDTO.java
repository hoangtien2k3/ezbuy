package com.ezbuy.productservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImportDTO {
    private boolean result;
    private String errMsg;
    private String id;
    private String code;
    private String name;
    private Double priceImport;
    private Double priceExport;
    private String unit;
    private String taxRatio;
    private Double discount;
    private Long revenueRatio;
    private String priceImportStr;
    private String priceExportStr;
    private String discountStr;
    private String revenueRatioStr;
}
