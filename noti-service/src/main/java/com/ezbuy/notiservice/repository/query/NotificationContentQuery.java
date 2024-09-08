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
package com.ezbuy.notiservice.repository.query;

public interface NotificationContentQuery {

    String insertNotificationContent =
            "INSERT INTO notification_content (id, title, sub_title, image_url, create_at, create_by, update_at, update_by, url, status)\n"
                    + "VALUES (:id, :title, :subTitle, :imageUrl, :createAt, :createBy, :updateAt, :updateBy, :url, :status);";
}
