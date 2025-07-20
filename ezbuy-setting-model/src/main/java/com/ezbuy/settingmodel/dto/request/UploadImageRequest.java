package com.ezbuy.settingmodel.dto.request;

import com.ezbuy.settingmodel.dto.FileDTO;
import java.util.List;
import lombok.Data;

@Data
public class UploadImageRequest {
    private String parentId;
    private List<FileDTO> images;
}
