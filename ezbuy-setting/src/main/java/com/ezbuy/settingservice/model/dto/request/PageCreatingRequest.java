package com.ezbuy.settingservice.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class PageCreatingRequest {
    @Size(max = 36, message = "page.id.invalid")
    private String id;

    @NotEmpty(message = "page.code.not.empty")
    private String code;

    @NotEmpty(message = "page.title.not.empty")
    @Size(max = 100, message = "page.title.invalid")
    private String title;

    private List<ContentDisplayRequest> components;
}
