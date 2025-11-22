package com.ezbuy.settingservice.model.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class StatusLockingRequest {
    @Size(max = 36, message = "telecomServiceId.invalid")
    private String id;

    @NonNull
    @Size(max = 1, min = 0)
    private Integer status;
}
