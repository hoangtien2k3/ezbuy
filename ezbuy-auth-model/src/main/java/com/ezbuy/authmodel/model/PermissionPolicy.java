package com.ezbuy.authmodel.model;

import com.ezbuy.authmodel.model.base.EntityBase;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "permission_policy")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PermissionPolicy extends EntityBase implements Persistable<String> {

    @Id
    private String id;
    private String type;
    private String value;
    private String code;
    private String description;
    private String keycloakId;
    private String keycloakName;
    private String policyId;
    private String individualOrganizationPermissionsId;
    private Integer status;
    private String ssoId;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
