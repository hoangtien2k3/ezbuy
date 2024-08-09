package com.ezbuy.auth.model.postgresql;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_credential")
@Builder
public class UserCredential implements Persistable<String> {
    @Id
    @Column("id")
    private String id;

    @Column("user_id")
    private String userId;

    @Column("username")
    private String username;

    @Column("hash_pwd")
    private String hashPwd;

    @Column("status")
    private Integer status;

    @Column("create_at")
    private LocalDateTime createAt;

    @Column("create_by")
    private String createBy;

    @Column("update_at")
    private LocalDateTime updateAt;

    @Column("update_by")
    private String updateBy;

    @Column("pwd_changed")
    private Integer pwdChanged;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }

    public UserCredential(UserCredential userCredential) {
        this.id = userCredential.id;
        this.userId = userCredential.userId;
        this.username = userCredential.username;
        this.hashPwd = userCredential.hashPwd;
        this.status = userCredential.status;
        this.createAt = userCredential.createAt;
        this.createBy = userCredential.createBy;
        this.updateAt = userCredential.updateAt;
        this.updateBy = userCredential.updateBy;
        this.pwdChanged = userCredential.pwdChanged;
        this.isNew = userCredential.isNew;
    }
}
