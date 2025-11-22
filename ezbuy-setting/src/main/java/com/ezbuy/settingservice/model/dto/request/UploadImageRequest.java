package com.ezbuy.settingservice.model.dto.request;

import com.ezbuy.settingservice.model.dto.FileDTO;
import java.util.List;
import lombok.Data;

@Data
public class UploadImageRequest {
    private String parentId;
    private List<FileDTO> images;
}
