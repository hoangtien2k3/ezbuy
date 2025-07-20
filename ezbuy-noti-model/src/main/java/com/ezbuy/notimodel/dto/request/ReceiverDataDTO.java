package com.ezbuy.notimodel.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ReceiverDataDTO {
    private String userId;
    private String email;

    public ReceiverDataDTO(String userId, String email) {
        this.userId = userId;
        this.email = userId;
    }
}
