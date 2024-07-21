package com.ezbuy.notification.repository;

import com.ezbuy.notification.model.Notification;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.ezbuy.notification.repository.query.NotificationQuery.insertNotification;

public interface NotificationRepository extends R2dbcRepository<Notification, String> {

    Mono<Notification> findById(String id);

    @Query(insertNotification)
    Mono<Void> insert(String id, String sender, String severity, String notificationContentId, String contentType, LocalDateTime createAt, String createBy, LocalDateTime updateAt, String updateBy, String categoryId, Integer status, LocalDateTime expectSendTime);

}
