package com.ezbuy.settingmodel.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePageStatusRequest {
    @NotEmpty
    private String pageId;

    @NotNull
    private Integer status;
}
