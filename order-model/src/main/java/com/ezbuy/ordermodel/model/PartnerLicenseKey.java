package com.ezbuy.ordermodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "partner_license_key")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PartnerLicenseKey implements Persistable<String> {
    private String id;

    private String serviceAlias;

    private String userName;

    private String userId;

    private String organizationId;

    private String licenseKey;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;

    private int status;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
