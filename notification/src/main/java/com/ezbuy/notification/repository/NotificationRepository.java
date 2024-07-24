package com.ezbuy.notification.repository;

import static com.ezbuy.notification.repository.query.NotificationQuery.insertNotification;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ezbuy.notification.model.Notification;

import reactor.core.publisher.Mono;

public interface NotificationRepository extends R2dbcRepository<Notification, String> {

    Mono<Notification> findById(String id);

    @Query(insertNotification)
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
