/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.settingmodel.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class ContentDisplayRequest {
    @Size(max = 36, message = "content-display.id.invalid")
    private String id;

    private String type;
    private String content;

    @Size(max = 36, message = "telecomServiceId.invalid")
    private String telecomServiceId;

    @Size(max = 36, message = "content-display.title.invalid")
    private String title;

    @Size(max = 36, message = "content-display.subtitle.invalid")
    private String subtitle;

    private String icon;
    private String iconBase64;

    @JsonIgnore
    private Boolean isUploadIcon;

    private String image;
    private String imageBase64;

    @JsonIgnore
    private Boolean isUploadImage;

    private String backgroundImage;
    private String backgroundBase64;

    @JsonIgnore
    private Boolean isUploadBackground;

    private Integer displayOrder;

    @Size(max = 36, message = "content-display.refUrl.invalid")
    private String refUrl;

    @Size(max = 36, message = "content-display.parentId.invalid")
    private String parentId;

    private Integer status;
    private Boolean isOriginal;
    private String name;
    private String description;
    private List<ContentDisplayRequest> contentDisplayDTOList;
}
