package com.ezbuy.notiservice.repository;

import com.ezbuy.notimodel.model.NotificationContent;
import com.ezbuy.notiservice.repository.query.NotificationContentQuery;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface NotificationContentRepository extends R2dbcRepository<NotificationContent, String> {
    Mono<NotificationContent> findById(String id);

    @Query(NotificationContentQuery.insertNotificationContent)
    Mono<Boolean> insert(
            String id,
            String title,
            String subTitle,
            String imageUrl,
            LocalDateTime createAt,
            String createBy,
            LocalDateTime updateAt,
            String updateBy,
            String url,
            Integer status);
}
