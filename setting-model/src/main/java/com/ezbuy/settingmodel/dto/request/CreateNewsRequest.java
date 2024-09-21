package com.ezbuy.settingmodel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNewsRequest extends SaveNewsRequest {
    @NotBlank
    private String sourceType;
}
