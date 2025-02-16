package com.ezbuy.productmodel.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherTypeRequest {
    private String code; // ma voucher type
    private String name; // ten voucher type
    private Integer priorityLevel; // do uu tien
    private String description; // mo ta
    private String actionType; // loai khuyen mai tac dong
    private String actionValue; // gia tri khuyen mai
    private String state; // trang thai vat ly
    private Integer status; // trang thai
    private String conditionUse; // dieu kien su dung
    private String payment; // phuong thuc thanh toan
    private LocalDate createFromDate; // tao tu ngay
    private LocalDate createToDate; // den ngay
}
