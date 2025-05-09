package com.ezbuy.ordermodel.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "invoice_info_history")
public class InvoiceInfoHistory implements Persistable<String> {
    @Id
    private String id;

    private String userId; // id user dang nhap
    private String organizationId; // id doanh nghiep
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
    private String createBy;
    private int status;
    private String organizationName;
    private String payType;
    private String accountNumber;
    private String updateLog;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
