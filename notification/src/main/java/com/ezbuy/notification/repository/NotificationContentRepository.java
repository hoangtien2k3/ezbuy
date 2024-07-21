package com.ezbuy.notification.repository;

import com.ezbuy.notification.model.NotificationContent;
import com.ezbuy.notification.repository.query.NotificationContentQuery;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface NotificationContentRepository extends R2dbcRepository<NotificationContent, String> {

    Mono<NotificationContent> findById(String id);

    @Query(NotificationContentQuery.insertNotificationContent)
    Mono<Boolean> insert(String id, String title, String subTitle, String imageUrl
            , LocalDateTime createAt, String createBy, LocalDateTime updateAt, String updateBy, String url, Integer status);

}
