package com.ezbuy.settingservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFileRequest {
    @NotBlank
    private String name;

    @NotNull
    private Integer type;

    private String parentId;
}
