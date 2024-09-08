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
package com.ezbuy.notimodel.model;

import com.ezbuy.notimodel.model.base.EntityBase;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "notification_content")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationContent extends EntityBase {
    private String id;
    private String title;
    private String subTitle;
    private String imageUrl;
    private String url;
    private Integer status;
    private String templateMail;
    private String externalData;

    public NotificationContent(
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy,
            String id,
            String title,
            String subTitle,
            String imageUrl,
            String url,
            Integer status,
            String externalData) {
        super(createAt, createBy, updateAt, updateBy);
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
        this.url = url;
        this.status = status;
        this.externalData = externalData;
    }

    public NotificationContent(
            String id,
            String title,
            String subTitle,
            String imageUrl,
            String url,
            Integer status,
            String externalData) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
        this.url = url;
        this.status = status;
        this.externalData = externalData;
    }

    public NotificationContent(String id, String title, String subTitle, String imageUrl, String url, Integer status) {}
}
