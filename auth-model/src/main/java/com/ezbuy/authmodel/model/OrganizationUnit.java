package com.ezbuy.authmodel.model;

import com.ezbuy.authmodel.model.base.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "organization_unit")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class OrganizationUnit extends EntityBase {

    @Id
    private String id;
    private String name;
    private String shortName;
    private String code;
    private String address;
    private String description;
    private String parentId;
    private String unitTypeId;
    private String organizationId;
    private Integer state;
    private Integer status;
    private String businessType;
    private String fieldOfActivity;
    private String presentativeId;
    private String placeName;
    private String tax;
}
