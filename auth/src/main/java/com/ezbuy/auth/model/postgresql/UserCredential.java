package com.ezbuy.auth.model.postgresql;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
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
public class UserCredential {
    @Id
    private String id;

    private String userId; // userId of user (mapping with id in user_entity)
    private String username; // user dang nhap
    private String hashPwd; // password hashed by HUB-SME
    private int status; // status of row
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
    private int pwdChanged;

    @Transient
    private boolean isNew = false;

    //    @Transient
    //    @Override
    //    public boolean isNew() {
    //        return this.isNew || id == null;
    //    }
}
