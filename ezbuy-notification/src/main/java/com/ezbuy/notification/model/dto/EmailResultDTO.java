package com.ezbuy.notification.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EmailResultDTO {
    private String transmissionId;
    private Boolean isSuccess;

    public EmailResultDTO(String transmissionId, Boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.transmissionId = transmissionId;
    }
}
