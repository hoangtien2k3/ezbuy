package com.ezbuy.auth.model.postgresql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "organization_unit")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrganizationUnit {

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

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;

    private String businessType;

    private String fieldOfActivity;

    private String presentativeId;

    private String placeName;

    private String tax;
}
