package com.ezbuy.settingmodel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UploadDTO {
    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String image;
}
