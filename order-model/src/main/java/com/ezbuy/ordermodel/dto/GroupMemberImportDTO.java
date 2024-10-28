package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMemberImportDTO {
    private boolean result; // ket qua import: true = thanh cong, false = that bai
    private String errMsg; // message import

    private String subIsdn; // thue bao thanh vien
    private String status; // trang thai
    private String addGroupDate; // ngay them vao nhom
    private String numberOfSign; // tong so luot ky

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    // public GroupMemberImportDTO(String subIsdn, String status, String
    // addGroupDate, String numberOfSign, boolean result) {
    // this.subIsdn = product.getId();
    // this.status = product.getCode();
    // this.addGroupDate = product.getName();
    // this.numberOfSign = product.getPriceImport();
    // this.errMsg = errMsg;
    // this.result = result;
    // }

    public void trim() {
        if (this.subIsdn != null) {
            this.subIsdn = this.subIsdn.trim();
        }
        if (this.numberOfSign != null) {
            this.numberOfSign = this.numberOfSign.trim();
        }
    }
}
