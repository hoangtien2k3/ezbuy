package com.ezbuy.notimodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotiContentDTO {
    @Size(message = "params.title.outOfLength", max = 500)
    @NotEmpty(message = "params.title.null")
    private String title;
    @Size(message = "params.subTitle.outOfLength", max = 5000)
    @NotEmpty(message = "params.subTitle.null")
    private String subTitle;
    @Size(message = "params.imageUrl.outOfLength", max = 500)
    private String imageUrl;
    @Size(message = "params.url.outOfLength", max = 300)
    private String url;
    @Size(message = "params.externalData.outOfLength")
    private String externalData;
}

