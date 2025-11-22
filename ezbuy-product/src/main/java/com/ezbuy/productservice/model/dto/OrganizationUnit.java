package com.ezbuy.productservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrganizationUnit {

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
