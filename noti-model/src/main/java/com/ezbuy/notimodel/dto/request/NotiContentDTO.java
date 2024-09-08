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
package com.ezbuy.notimodel.dto.request;

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

    @Size(message = "params.externalData.outOfLength")
    private String externalData;
}
