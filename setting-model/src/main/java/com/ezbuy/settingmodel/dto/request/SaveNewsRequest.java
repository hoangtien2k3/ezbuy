package com.ezbuy.settingmodel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SaveNewsRequest {
    @NotNull
    private Integer newsType;

    @NotBlank
    private String image;

    @NotBlank
    @Length(max = 200)
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    @Length(max = 2000)
    private String path;
}
