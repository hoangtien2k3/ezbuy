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
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Table(name = "individual")
public class Individual extends EntityBase implements Persistable<String> {
    @Id
    @Column("id")
    private String id;

    @Column("user_id")
    @Length(max = 36, message = "individual.userId.over.length")
    private String userId;

    @Column("username")
    @Length(max = 50, message = "individual.username.over.length")
    private String username;

    @Column("name")
    @Length(max = 255, message = "individual.name.over.length")
    private String name;

    @Column("code")
    private String code;

    @Column("image")
    @Length(max = 500, message = "individual.image.over.length")
    private String image;

    @Column("gender")
    @Length(max = 10, message = "individual.gender.over.length")
    private String gender;

    @Column("birthday")
    private LocalDateTime birthday;

    @Column("email")
    @Length(max = 200, message = "individual.email.over.length")
    private String email;

    @Column("email_account")
    @Length(max = 200, message = "individual.emailAccount.over.length")
    private String emailAccount;

    @Column("phone")
    @Length(max = 20, message = "individual.phone.over.length")
    private String phone;

    @Column("address")
    @Length(max = 255, message = "individual.address.over.length")
    private String address;

    @Column("province_code")
    @Length(max = 5, message = "individual.provinceCode.over.length")
    private String provinceCode;

    @Column("district_code")
    @Length(max = 5, message = "individual.districtCode.over.length")
    private String districtCode;

    @Column("precinct_code")
    @Length(max = 5, message = "individual.precinctCode.over.length")
    private String precinctCode;

    @Column("status")
    @Max(value = 99, message = "individual.status.over.length")
    private Integer status;

    @Column("position_code")
    @Length(max = 20, message = "individual.posisionCode.over.length")
    private String positionCode;

    @Column("password_change")
    private Boolean passwordChange;

    @Column("probation_day")
    private LocalDateTime probationDay;

    @Column("start_working_day")
    private LocalDateTime startWorkingDay;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }

    public Individual(Individual individual) {
        this.id = individual.id;
        this.userId = individual.userId;
        this.username = individual.username;
        this.name = individual.name;
        this.code = individual.code;
        this.image = individual.image;
        this.gender = individual.gender;
        this.birthday = individual.birthday;
        this.email = individual.email;
        this.emailAccount = individual.emailAccount;
        this.phone = individual.phone;
        this.address = individual.address;
        this.provinceCode = individual.provinceCode;
        this.districtCode = individual.districtCode;
        this.precinctCode = individual.precinctCode;
        this.status = individual.status;
        this.setCreateAt(individual.getCreateAt());
        this.setCreateBy(individual.getCreateBy());
        this.setUpdateAt(individual.getUpdateAt());
        this.setUpdateBy(individual.getUpdateBy());
        this.positionCode = individual.positionCode;
        this.passwordChange = individual.passwordChange;
        this.isNew = individual.isNew;
        this.probationDay = individual.probationDay;
        this.startWorkingDay = individual.startWorkingDay;
    }
}
