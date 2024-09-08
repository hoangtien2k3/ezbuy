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
package com.ezbuy.notiservice.repository;

import com.ezbuy.notimodel.model.Notification;
import com.ezbuy.notiservice.repository.query.NotificationQuery;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface NotificationRepository extends R2dbcRepository<Notification, String> {

    Mono<Notification> findById(String id);

    @Query(NotificationQuery.insertNotification)
    Mono<Void> insert(
            String id,
            String sender,
            String severity,
            String notificationContentId,
            String contentType,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy,
            String categoryId,
            Integer status,
            LocalDateTime expectSendTime);
}
