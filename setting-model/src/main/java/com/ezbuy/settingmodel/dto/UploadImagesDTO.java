package com.ezbuy.settingmodel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadImagesDTO {
    private String id;
    private String name;
    private Integer type;
    private String parentId;
    private String path;
    private Integer status;
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private LocalDateTime updateBy;
    private List<FileDTO> files;
    private Long totalImages;
    private List<String> previewImages;
    private List<UploadImagesDTO> children;
}
