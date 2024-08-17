package com.ezbuy.settingmodel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime updateAt;
    private List<FileDTO> files;
    private Long totalImages;
    private List<String> previewImages;
    private List<UploadImagesDTO> children;
}
