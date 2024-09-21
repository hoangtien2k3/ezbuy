package com.ezbuy.authmodel.model;

import com.ezbuy.authmodel.model.base.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "individual_organization_permissions")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class IndividualOrganizationPermissions extends EntityBase {
    private String id;
    private String individualId;
    private String organizationId;
    private String clientId;
    private Integer status;
}
