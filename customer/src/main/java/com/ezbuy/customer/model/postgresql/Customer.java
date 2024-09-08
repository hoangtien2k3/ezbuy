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
@Table("customer")
public class Customer {
    @Id
    private Long id; // id

    @Column("customer_group_id")
    private Long customerGroupId; // id group

    private String email; // email
    private String username; // ten dang nhap
    private String password; // mat khau

    @Column("first_name")
    private String firstName; // mat khau

    @Column("last_name")
    private String lastName; // mat khau

    @Column("display_name")
    private String displayName; // ten hien thi

    private LocalDateTime birthday; // ngay sinh

    @Column("image_url")
    private String imageUrl; // link anh

    @Column("phone_number")
    private String phoneNumber; // so dien thoai

    private String gender; // gioi tinh

    @Column("created_at")
    private LocalDateTime createdAt; // thoi gian tao

    @Column("updated_at")
    private LocalDateTime updatedAt; // thoi gian cap nhat

    @Column("email_verified")
    private Integer emailVerified; // xac thuc email

    @Column("subscribed_to_newsletter")
    private Boolean subscribedToNewsletter; // dang ky nhan thong bao
}
