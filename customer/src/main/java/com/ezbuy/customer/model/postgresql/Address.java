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
@Table("address")
public class Address {
    @Id
    private Long id; // id

    @Column("customer_id")
    private Long customerId; // id khach hang

    private String street; // ten duong
    private String province; // tinh
    private String district; // huyen
    private String precinct; // xa
    private String city; // thanh pho

    @Column("address_detail")
    private String addressDetail;

    private String postCode; // ma buu dien

    @Column("country_code")
    private String countryCode; // ma quoc gia

    @Column("created_at")
    private LocalDateTime createdAt; // thoi gian tao

    @Column("updated_at")
    private LocalDateTime updatedAt; // thoi gian cap nhat

    private String company; // ten cong ty
}
