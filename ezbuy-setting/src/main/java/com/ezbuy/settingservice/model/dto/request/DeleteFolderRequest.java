package com.ezbuy.settingservice.model.dto.request;

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
