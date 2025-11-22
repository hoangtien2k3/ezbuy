package com.ezbuy.settingservice.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class UpdateImageRequest {
    @Valid
    @NotEmpty
    private List<UploadDTO> images;
}
