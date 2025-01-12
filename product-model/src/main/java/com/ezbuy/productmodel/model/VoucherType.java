package com.ezbuy.productmodel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "voucher_type")
public class VoucherType implements Persistable<String> {
    @Id
    private String id;//id voucher type
    private String code;//voucher type code
    private String name;//ten voucher type
    private Integer priorityLevel;//do uu tien
    private String description;//mo ta chi tiet
    private String actionType;//ma tac dong
    private String actionValue;//gia tri tac dong
    private String payment;//phuong thuc thanh toan cua voucher su dung
    private String state;//trang thai vat ly
    private Integer status;// trang thai
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
    private String conditionUse;//dieu kien su dung
    @Transient
    @JsonIgnore
    private boolean isNew = false;

    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }

    public VoucherType(VoucherType voucherType) {
        this.id = voucherType.id;
        this.code = voucherType.code;
        this.name = voucherType.name;
        this.priorityLevel = voucherType.priorityLevel;
        this.description = voucherType.description;
        this.actionType = voucherType.actionType;
        this.actionValue = voucherType.actionValue;
        this.payment = voucherType.payment;
        this.state = voucherType.state;
        this.status = voucherType.status;
        this.createAt = voucherType.createAt;
        this.createBy = voucherType.createBy;
        this.updateAt = voucherType.updateAt;
        this.updateBy = voucherType.updateBy;
        this.conditionUse = voucherType.conditionUse;
    }
}
