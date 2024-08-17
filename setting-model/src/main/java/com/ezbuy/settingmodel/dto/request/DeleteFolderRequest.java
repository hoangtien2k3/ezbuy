package com.ezbuy.settingmodel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteFolderRequest {
    @NotBlank
    private String id;
    @NotNull
    private Integer deleteType;
}
