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
package com.ezbuy.customer.model.postgresql;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("shop_user")
public class ShopUser {
    @Id
    private Long id; // id

    @Column("customer_id")
    private Long customerId; // id khach hang

    @Column("status_view")
    private Integer statusView; // trang thai xem

    @Column("status_account")
    private Integer statusAccount; // trang thai tai khoan

    @Column("last_login")
    private LocalDateTime lastLogin; // thoi gian dang nhap cuoi cung

    @Column("expired_at")
    private LocalDateTime expiresAt; // thoi gian het han

    private String roles; // vai tro

    @Column("security_question")
    private String securityQuestion; // cau hoi bao mat

    @Column("security_answer")
    private String securityAnswer; // cau tra loi bao mat

    @Column("created_at")
    private LocalDateTime createdAt; // thoi gian tao

    @Column("updated_at")
    private LocalDateTime updatedAt; // thoi gian cap nhat

    @Column("loyalty_points")
    private Integer loyaltyPoints; // diem tich luy
}
