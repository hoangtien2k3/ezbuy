package com.ezbuy.settingmodel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteImageRequest {
    @NotBlank
    private String id;

    private String parentId;
}
