package com.ezbuy.settingmodel.dto.request;

import com.ezbuy.settingmodel.dto.FileDTO;
import lombok.Data;

import java.util.List;

@Data
public class UploadImageRequest {
    private String parentId;
    private List<FileDTO> images;
}
