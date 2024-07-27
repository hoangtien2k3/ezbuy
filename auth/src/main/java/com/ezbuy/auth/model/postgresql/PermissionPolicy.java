package com.ezbuy.auth.model.postgresql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "permission_policy")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PermissionPolicy implements Persistable<String> {

    @Id
    @org.springframework.data.annotation.Id
    private String id;

    private String type;

//    private String value;

    private String code;

//    private String description;

    private String keycloakId;

    private String keycloakName;

    private String policyId;

    private String individualOrganizationPermissionsId;

    private Integer status;

//    private String ssoId;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
