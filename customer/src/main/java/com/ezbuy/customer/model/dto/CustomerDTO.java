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
package com.ezbuy.customer.model.dto;

import com.ezbuy.customer.model.postgresql.Customer;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CustomerDTO(
        Long id,
        Long customerGroupId,
        String email,
        String username,
        String firstName,
        String lastName,
        String displayName,
        LocalDateTime birthday,
        String imageUrl,
        String phoneNumber,
        String gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Integer isVerifiedEmail,
        Boolean subscribedToNewsletter) {
    public static CustomerDTO fromModel(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .customerGroupId(customer.getCustomerGroupId())
                .email(customer.getEmail())
                .username(customer.getUsername())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .displayName(customer.getDisplayName())
                .birthday(customer.getBirthday())
                .imageUrl(customer.getImageUrl())
                .phoneNumber(customer.getPhoneNumber())
                .gender(customer.getGender())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .isVerifiedEmail(customer.getEmailVerified())
                .subscribedToNewsletter(customer.getSubscribedToNewsletter())
                .build();
    }
}
