package com.ezbuy.settingservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RenameFolderRequest {
    @NotBlank
    private String id;

    @NotBlank
    private String newName;
}
