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
package com.ezbuy.notimodel.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationDTO {
    @Size(message = "params.sender.outOfLength", max = 500)
    @NotEmpty(message = "params.sender.null")
    private String sender; // ezbuy@gmail.com || hoangtien2k3qx1@gmail.com

    @Size(message = "params.severity.outOfLength", max = 50)
    private String severity; // NORMAL || CRITICAL

    private NotiContentDTO notiContentDTO; // IS NONE NULL

    @Size(message = "params.contentType.outOfLength", max = 100)
    @NotEmpty(message = "params.contentType.null")
    private String contentType; // text/plain || html/plain

    @Size(message = "params.categoryType.outOfLength", max = 100)
    @NotEmpty(message = "params.categoryType.null")
    private String categoryType; // ANNOUNCEMENT || NEWS

    @Size(message = "params.channelType.outOfLength", max = 100)
    @NotEmpty(message = "params.channelType.null")
    private String channelType; // EMAIL || SMS || REST

    private String templateMail; // SIGN_UP || FORGOT_PASSWORD || SIGN_UP_PASSWORD || CUSTOMER_ACTIVE_SUCCESS ||
    // CUSTOMER_REGISTER_SUCCESS || EMPLOYEE_REGISTER_SUCCESS || ACCOUNT_ACTIVE ||
    // VERIFY_ACCOUNT_SUCESS || NOTI_VERIFY_ACCOUNT
    private LocalDateTime expectSendTime;
    private List<ReceiverDataDTO> receiverList; // IS NONE NULL
    private Boolean sendAll; // true || false || null (default is false)
}
