package com.ezbuy.productservice.model.dto;

import com.ezbuy.productservice.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImportDTO {
    private boolean result; // ket qua import: true = thanh cong, false = that bai
    private String errMsg; // message import

    private String id;
    private String code; // ma hang hoa
    private String name; // ten hang hoa
    private Double priceImport; // don gia nhap
    private Double priceExport; // don gia ban
    private String unit; // don vi tinh
    private String taxRatio; // thue GTGT
    private Double discount; // chiet khau
    private Long revenueRatio; // ti le % theo doanh thu

    private String priceImportStr; // don gia nhap string
    private String priceExportStr; // don gia ban string
    private String discountStr; // chiet khau string
    private String revenueRatioStr; // ti le % theo doanh thu string

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public ProductImportDTO(Product product, String errMsg, boolean result) {
        this.id = product.getId();
        this.code = product.getCode();
        this.name = product.getName();
        this.priceImport = product.getPriceImport();
        this.priceExport = product.getPriceExport();
        this.unit = product.getUnit();
        this.taxRatio = product.getTaxRatio();
        this.taxRatio = product.getTaxRatio();
        this.priceExport = product.getDiscount();
        this.revenueRatio = product.getRevenueRatio();
        this.errMsg = errMsg;
        this.result = result;
    }

    public void trim() {
        if (this.code != null) {
            this.code = this.code.trim();
        }
        if (this.name != null) {
            this.name = this.name.trim();
        }
        if (this.unit != null) {
            this.unit = this.unit.trim();
        }
        if (this.priceImportStr != null) {
            this.priceImportStr = this.priceImportStr.trim();
        }
        if (this.priceExportStr != null) {
            this.priceExportStr = this.priceExportStr.trim();
        }
        if (this.taxRatio != null) {
            this.taxRatio = this.taxRatio.trim();
        }
        if (this.discountStr != null) {
            this.discountStr = this.discountStr.trim();
        }
        if (this.revenueRatioStr != null) {
            this.revenueRatioStr = this.revenueRatioStr.trim();
        }
    }
}
