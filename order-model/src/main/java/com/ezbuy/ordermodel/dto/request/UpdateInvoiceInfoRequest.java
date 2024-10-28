package com.ezbuy.ordermodel.dto.request;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UpdateInvoiceInfoRequest {
    private String id;
    private String userId; // id user dang nhap
    private String organizationId; // id doanh nghiep
    private String organizationName; // ten doanh nghiep
    private String taxCode; // ma so thue
    private String fullName; // ho va ten
    private String phone; // so dien thoai
    private String email; // email
    private String provinceCode;
    private String provinceName;
    private String districtCode;
    private String districtName;
    private String precinctCode;
    private String precinctName;
    private String addressDetail; // dia chi chi tiet
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;
    private int status;
    private String payType; // hinh thuc chuyen khoan
    private String accountNumber; // so tai khoan
}
