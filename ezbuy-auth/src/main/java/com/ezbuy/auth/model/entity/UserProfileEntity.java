package com.ezbuy.auth.model.entity;

import com.ezbuy.auth.model.entity.base.BaseEntity;
import java.time.LocalDate;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "user_profile")
public class UserProfileEntity extends BaseEntity implements Persistable<String> {

    @Id
    private String id;
    private String image;
    private String companyName;
    private String representative;
    private String phone;
    private String taxCode;
    private String taxDepartment;
    private LocalDate foundingDate;
    private String businessType;
    private String provinceCode;
    private String districtCode;
    private String precinctCode;
    private String streetBlock;
    private String userId;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
