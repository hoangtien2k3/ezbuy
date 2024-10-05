package com.ezbuy.ordermodel.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateInvoiceInfoHistoryRequest {
    private String id;
    private String userId; //id user dang nhap
    private String organizationId; //id doanh nghiep
    private String taxCode; //ma so thue
    private String fullName; //ho va ten
    private String phone; //so dien thoai
    private String email; //email
    private String provinceCode;
    private String provinceName;
    private String districtCode;
    private String districtName;
    private String precinctCode;
    private String precinctName;
    private String addressDetail; //dia chi chi tiet
    private LocalDateTime createAt;
    private String createBy;
    private String organizationName;
    private String payType;
    private String accountNumber;
}
