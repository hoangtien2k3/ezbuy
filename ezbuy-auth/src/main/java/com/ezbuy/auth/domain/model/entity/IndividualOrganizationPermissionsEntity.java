package com.ezbuy.auth.domain.model.entity;

import com.ezbuy.auth.domain.model.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "individual_organization_permissions")
public class IndividualOrganizationPermissionsEntity extends BaseEntity {
    private String id;
    private String individualId;
    private String organizationId;
    private String clientId;
    private Integer status;
}
