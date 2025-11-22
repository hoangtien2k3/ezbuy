package com.ezbuy.auth.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * SIGN_UP FORGOT_PASSWORD SIGN_UP_PASSWORD CUSTOMER_ACTIVE_SUCCESS
     * CUSTOMER_REGISTER_SUCCESS EMPLOYEE_REGISTER_SUCCESS ACCOUNT_ACTIVE
     * VERIFY_ACCOUNT_SUCESS NOTI_VERIFY_ACCOUNT
     */
    private String templateMail;

    private LocalDateTime expectSendTime;
    private List<ReceiverDataDTO> receiverList; // IS NONE NULL
    private Boolean sendAll; // true || false || null (default is false)
}
