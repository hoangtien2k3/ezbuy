package com.ezbuy.auth.dto.request;

import com.ezbuy.customer.model.NotiContentDTO;
import com.ezbuy.customer.model.ReceiverDataDTO;
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
    private String sender;

    @Size(message = "params.severity.outOfLength", max = 50)
    private String severity;

    private NotiContentDTO notiContentDTO;

    @Size(message = "params.contentType.outOfLength", max = 100)
    @NotEmpty(message = "params.contentType.null")
    private String contentType;

    @Size(message = "params.categoryType.outOfLength", max = 100)
    @NotEmpty(message = "params.categoryType.null")
    private String categoryType;

    @Size(message = "params.channelType.outOfLength", max = 100)
    @NotEmpty(message = "params.channelType.null")
    private String channelType;

    private String templateMail;
    private LocalDateTime expectSendTime;
    private List<ReceiverDataDTO> receiverList;
    private Boolean sendAll;
}
