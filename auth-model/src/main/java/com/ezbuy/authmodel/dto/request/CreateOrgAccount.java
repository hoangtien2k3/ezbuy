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
package com.ezbuy.authmodel.dto.request;

import com.ezbuy.authmodel.dto.response.IndividualDTO;
import com.ezbuy.authmodel.model.TenantIdentify;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrgAccount {
    @NotEmpty(message = "dto.email.not.empty")
    @Size(max = 200, message = "dto.email.over.length")
    private String email;

    @Length(max = 12, message = "organization.phone.over.length")
    @NotEmpty(message = "organization.phone.not.empty")
    private String phone;

    @Length(max = 255, message = "organization.name.over.length")
    @NotEmpty(message = "organization.name.not.empty")
    private String name;

    private LocalDateTime foundingDate;

    @NotNull(message = "organization.representative.null")
    private IndividualDTO representative;

    @NotNull(message = "organization.identify.null")
    private List<TenantIdentify> identifies;
}
