package com.ezbuy.productmodel.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherTypeDTO {
    private String id; // id
    private String code; // code
    private String name; // ten loai khuyen mai
    private Integer priorityLevel; // do uu tien
    private String description; // mo ta chi tiet
    private String actionType; // cach thuc khuyen mai
    private String actionValue; // gia tri khuyen mai
    private String state; // trang thai vat ly cua loai khuyen mai( ACTIVE, INACTIVE)
    private Integer status; // trang thai cua loai khuyen mai( 1- active, 0- inactive)
    private String payment; // phuong thuc thanh toan cua voucher su dung
    private String conditionUse; // dieu kien su dung
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;
}
