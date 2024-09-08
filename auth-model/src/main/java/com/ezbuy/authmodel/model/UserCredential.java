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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_credential")
@SuperBuilder
public class UserCredential extends EntityBase implements Persistable<String> {
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

    public UserCredential(UserCredential userCredential) {
        this.id = userCredential.id;
        this.userId = userCredential.userId;
        this.username = userCredential.username;
        this.hashPwd = userCredential.hashPwd;
        this.status = userCredential.status;
        this.setCreateAt(userCredential.getCreateAt());
        this.setCreateBy(userCredential.getCreateBy());
        this.setUpdateAt(userCredential.getUpdateAt());
        this.setUpdateBy(userCredential.getUpdateBy());
        this.pwdChanged = userCredential.pwdChanged;
        this.isNew = userCredential.isNew;
    }
}
