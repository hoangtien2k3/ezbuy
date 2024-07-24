package com.ezbuy.customer.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
