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

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmOTPRequest {
    @NotEmpty(message = "dto.otp.email.not.empty")
    @Size(max = 200, message = "dto.otp.email.over.length")
    private String email; // doi tuong trong bang user_otp
    // @NotEmpty(message = "dto.otp.not.empty")
    private String otp; // doi tuong trong bang user_otp

    @Size(max = 50, message = "dto.otp.type.over.length")
    @NotEmpty(message = "dto.otp.type.not.empty")
    private String type; // doi tuong trong bang user_otp
}
