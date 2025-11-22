package com.ezbuy.settingservice.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeNewsStatusRequest {
    @NotNull
    private Integer toStatus;
}
