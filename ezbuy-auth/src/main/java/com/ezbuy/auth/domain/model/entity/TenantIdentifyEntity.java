package com.ezbuy.auth.domain.model.entity;

import com.ezbuy.auth.domain.model.entity.base.BaseEntity;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "tenant_identify")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TenantIdentifyEntity extends BaseEntity implements Persistable<String> {

    @Id
    private String id;

    @Length(max = 255, message = "identify.idType.over.length")
    private String idType;

    @Length(max = 255, message = "identify.idNo.over.length")
    private String idNo;

    @Length(max = 255, message = "identify.taxDepartment.over.length")
    private String taxDepartment;

    private LocalDateTime issueDate;

    @Length(max = 255, message = "identify.issuedBy.over.length")
    private String issuedBy;

    private LocalDateTime expirationDate;

    @Length(max = 1000, message = "identify.note.over.length")
    private String note;

    @Max(value = 99, message = "identify.primaryIdentify.over.length")
    private Integer primaryIdentify;

    @Max(value = 99, message = "identify.trustStatus.over.length")
    private Integer trustStatus;

    @Length(max = 100, message = "identify.type.over.length")
    private String type;

    @Length(max = 36, message = "identify.tenantId.over.length")
    private String tenantId;

    @Max(value = 99, message = "identify.status.over.length")
    private Integer status;

    @Length(max = 255, message = "identify.tenantId.over.length")
    private String certificate;

    @Length(max = 255, message = "identify.tenantId.over.length")
    private String trustFile;

    @Length(max = 36, message = "identify.tenantId.over.length")
    private String transactionId;

    private String requestCode;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
