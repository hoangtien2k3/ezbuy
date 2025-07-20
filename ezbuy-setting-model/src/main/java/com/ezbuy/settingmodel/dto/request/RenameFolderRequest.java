package com.ezbuy.settingmodel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RenameFolderRequest {
    @NotBlank
    private String id;

    @NotBlank
    private String newName;
}
