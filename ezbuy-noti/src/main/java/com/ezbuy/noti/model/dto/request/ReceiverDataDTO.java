package com.ezbuy.noti.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReceiverDataDTO {
    private String userId;
    private String email;
}
