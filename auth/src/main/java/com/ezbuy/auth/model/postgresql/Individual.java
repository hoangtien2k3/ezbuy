package com.ezbuy.auth.model.postgresql;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "individual")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Individual {

    @Id
    private String id;

    @Length(max = 36, message = "individual.userId.over.length")
    private String userId;

    @Length(max = 50, message = "individual.username.over.length")
    private String username;

    @Length(max = 255, message = "individual.name.over.length")
    private String name;

    private String code;

    @Length(max = 500, message = "individual.image.over.length")
    private String image;

    @Length(max = 10, message = "individual.gender.over.length")
    private String gender;

    private LocalDateTime birthday;

    @Length(max = 200, message = "individual.email.over.length")
    private String email;

    @Length(max = 200, message = "individual.emailAccount.over.length")
    private String emailAccount;

    @Length(max = 20, message = "individual.phone.over.length")
    private String phone;

    @Length(max = 255, message = "individual.address.over.length")
    private String address;

    @Length(max = 5, message = "individual.provinceCode.over.length")
    private String provinceCode;

    @Length(max = 5, message = "individual.districtCode.over.length")
    private String districtCode;

    @Length(max = 5, message = "individual.precinctCode.over.length")
    private String precinctCode;

    @Max(value = 99, message = "individual.status.over.length")
    private Integer status;

    private LocalDateTime createAt;

    @Length(max = 255, message = "individual.createBy.over.length")
    private String createBy;

    private LocalDateTime updateAt;

    @Length(max = 255, message = "individual.updateBy.over.length")
    private String updateBy;

    @Length(max = 20, message = "individual.posisionCode.over.length")
    private String positionCode;

    private Boolean passwordChange;

    @Transient
    private boolean isNew = false;

    //    @Transient
    //    @Override
    //    public boolean isNew() {
    //        return this.isNew || id == null;
    //    }

    private LocalDateTime probationDay;

    private LocalDateTime startWorkingDay;

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
        this.createAt = individual.createAt;
        this.createBy = individual.createBy;
        this.updateAt = individual.updateAt;
        this.updateBy = individual.updateBy;
        this.positionCode = individual.positionCode;
        this.passwordChange = individual.passwordChange;
        this.isNew = individual.isNew;
        this.probationDay = individual.probationDay;
        this.startWorkingDay = individual.startWorkingDay;
    }
}
