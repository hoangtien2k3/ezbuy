package com.ezbuy.auth.model.postgresql;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_otp")
@Builder
public class UserOtp implements Persistable<String> {
    @Id
    private String id;
    private String type;
    private String email;
    private String otp;
    private LocalDateTime expTime;
    private Integer tries;
    private Integer status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;

    @Transient
    private boolean newOtp;

    @Transient
    @Override
    public boolean isNew() {
        return this.newOtp || id == null;
    }

    public UserOtp setAsNew() {
        this.newOtp = true;
        return this;
    }

}
