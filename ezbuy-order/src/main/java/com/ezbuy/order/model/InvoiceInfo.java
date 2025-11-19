package com.ezbuy.order.model;

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

    private String userId;
    private String organizationId;
    private String taxCode;
    private String fullName;
    private String phone;
    private String email;
    private String provinceCode;
    private String provinceName;
    private String districtCode;
    private String districtName;
    private String precinctCode;
    private String precinctName;
    private String addressDetail;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;
    private int status;
    private String organizationName;
    private String payType;
    private String accountNumber;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
