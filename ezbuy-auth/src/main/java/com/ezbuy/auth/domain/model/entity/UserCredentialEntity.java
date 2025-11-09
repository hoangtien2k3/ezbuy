package com.ezbuy.auth.domain.model.entity;

import com.ezbuy.auth.domain.model.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_credential")
@SuperBuilder
public class UserCredentialEntity extends BaseEntity implements Persistable<String> {

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

    @Column("pwd_changed")
    private Integer pwdChanged;

    @Transient
    private boolean isNew = false;

    @Transient
    @Override
    public boolean isNew() {
        return this.isNew || id == null;
    }
}
