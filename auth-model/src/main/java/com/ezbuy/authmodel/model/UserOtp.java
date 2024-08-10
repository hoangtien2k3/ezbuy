package com.ezbuy.authmodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_otp")
@Builder
public class UserOtp implements Persistable<String> {
    @Id
    @Column("id")
    private String id;

    @Column("type")
    private String type;

    @Column("email")
    private String email;

    @Column("otp")
    private String otp;

    @Column("exp_time")
    private LocalDateTime expTime;

    @Column("tries")
    private Integer tries;

    @Column("status")
    private Integer status;

    @Column("create_at")
    private LocalDateTime createAt;

    @Column("update_at")
    private LocalDateTime updateAt;

    @Column("create_by")
    private String createBy;

    @Column("update_by")
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
