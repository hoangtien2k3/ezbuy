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

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCreateRequest {
    private FileDTO image;
    private String name;
    private String phone;
    private String email;
    private LocalDateTime birthday;
    private String gender;
    private String code;
    private Integer status;
    private String address;
    private List<EmployeePositionRequest> employeePositionRequestList;
    private LocalDateTime probationDay;
    private LocalDateTime startWorkingDay;
    private String username;
    private String emailAccount;
    private Boolean sendEmail;
    private Integer accountStatus;
    private List<EmployeePermissionRequest> employeePermissionRequestList;
    private Boolean isEditable;
}
