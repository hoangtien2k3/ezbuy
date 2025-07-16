package com.ezbuy.authmodel.model;

import com.ezbuy.authmodel.model.base.EntityBase;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "individual_organization_permissions")
public class IndividualOrganizationPermissions extends EntityBase {
    private String id;
    private String individualId;
    private String organizationId;
    private String clientId;
    private Integer status;
}
