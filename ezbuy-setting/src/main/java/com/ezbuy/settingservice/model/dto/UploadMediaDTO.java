package com.ezbuy.settingservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadMediaDTO {
    private String url; // duong dan minio
    private String extend; // duoi file
    private Boolean status; // ket qua upload

    public UploadMediaDTO(String url, String extend) {
        this.url = url;
        this.extend = extend;
    }
}
