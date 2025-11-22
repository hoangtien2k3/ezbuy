package com.ezbuy.productservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "voucher_type")
public class VoucherType implements Persistable<String> {

    @Id
    private String id;
    private String code;
    private String name;
    private Integer priorityLevel;
    private String description;
    private String actionType;
    private String actionValue;
    private String payment;
    private String state;
    private Integer status;
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
    private String conditionUse;

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
