package com.ezbuy.ordermodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "invoice_info")
public class InvoiceInfo implements Persistable<String> {
    @Id
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
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;
    private int status;
    private String organizationName;
    private String payType; //hinh thuc chuyen khoan
    private String accountNumber; //so tai khoan

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
