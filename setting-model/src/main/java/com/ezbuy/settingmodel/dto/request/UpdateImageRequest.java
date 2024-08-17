package com.ezbuy.settingmodel.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateImageRequest {
    @Valid
    @NotEmpty
    private List<UploadDTO> images;
}
