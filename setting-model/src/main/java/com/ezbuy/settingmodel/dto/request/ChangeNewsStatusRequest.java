package com.ezbuy.settingmodel.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeNewsStatusRequest {
    @NotNull
    private Integer toStatus;
}
