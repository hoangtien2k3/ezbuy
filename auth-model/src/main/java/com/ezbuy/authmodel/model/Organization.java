package com.ezbuy.authmodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "organization")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Organization {

    private String id;

    private String name;

    private String image;

    private String businessType;

    private LocalDateTime foundingDate;

    private String email;

    private String phone;

    private String provinceCode;

    private String districtCode;

    private String precinctCode;

    private String streetBlock;

    private Integer state;

    private Integer status;

    private LocalDateTime createAt;

    private String createBy;

    private LocalDateTime updateAt;

    private String updateBy;

    private String orgType; // loai khach hang

    private String taxDepartment; // chi cuc thue quan ly

    public Organization(Organization organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.image = organization.getImage();
        this.businessType = organization.getBusinessType();
        this.foundingDate = organization.getFoundingDate();
        this.email = organization.getEmail();
        this.phone = organization.getPhone();
        this.provinceCode = organization.getProvinceCode();
        this.districtCode = organization.getDistrictCode();
        this.precinctCode = organization.getPrecinctCode();
        this.streetBlock = organization.getStreetBlock();
        this.state = organization.getState();
        this.status = organization.getStatus();
        this.createAt = organization.getCreateAt();
        this.createBy = organization.getCreateBy();
        this.updateAt = organization.getUpdateAt();
        this.updateBy = organization.getUpdateBy();
        this.orgType = organization.getOrgType();
        this.taxDepartment = organization.getTaxDepartment();
    }
}

