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
package com.ezbuy.settingmodel.model;

import com.ezbuy.settingmodel.model.base.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentDisplay extends EntityBase implements Persistable<String> {
    @Id
    private String id;

    private String pageId;
    private String type;
    private String content;
    private String telecomServiceId;
    private String serviceAlias;
    private String title;
    private String subtitle;
    private String icon;
    private String image;
    private String backgroundImage;
    private Integer displayOrder;
    private String refUrl;
    private String parentId;
    private Boolean isOriginal;
    private String name;
    private String description;
    private String screenUrl;

    @Transient
    private boolean insert;

    @Override
    public boolean isNew() {
        return insert || id == null;
    }
}
