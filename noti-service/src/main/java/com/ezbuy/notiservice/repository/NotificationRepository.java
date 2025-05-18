package com.ezbuy.notiservice.repository;

import com.ezbuy.notimodel.model.Notification;
import com.ezbuy.notiservice.repository.query.NotificationQuery;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface NotificationRepository extends R2dbcRepository<Notification, String> {

    @Query(NotificationQuery.INSERT_NOTIFICATION)
    Mono<Void> insert(String id, String sender, String severity, String notificationContentId, String contentType, LocalDateTime createAt, String createBy, LocalDateTime updateAt, String updateBy, String categoryId, Integer status, LocalDateTime expectSendTime);
}
