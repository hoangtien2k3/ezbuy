/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.authmodel.model;

import com.ezbuy.authmodel.model.base.EntityBase;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "organization")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Organization extends EntityBase {
    @Id
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

    // private LocalDateTime createAt;
    //
    // private String createBy;
    //
    // private LocalDateTime updateAt;
    //
    // private String updateBy;

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
        this.setCreateAt(organization.getCreateAt());
        this.setCreateBy(organization.getCreateBy());
        this.setUpdateAt(organization.getUpdateAt());
        this.setUpdateBy(organization.getUpdateBy());
        this.orgType = organization.getOrgType();
        this.taxDepartment = organization.getTaxDepartment();
    }
}
