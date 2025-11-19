package com.ezbuy.auth.model.entity;

import com.ezbuy.auth.model.entity.base.BaseEntity;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "organization")
public class OrganizationEntity extends BaseEntity {

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
    private String orgType;
    private String taxDepartment;
}
