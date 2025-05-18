package com.ezbuy.notiservice.repository;

import com.ezbuy.notimodel.model.NotificationContent;
import com.ezbuy.notiservice.repository.query.NotificationContentQuery;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface NotificationContentRepository extends R2dbcRepository<NotificationContent, String> {

    @Query(NotificationContentQuery.INSERT_NOTIFICATION_CONTENT)
    Mono<Boolean> insert(String id, String title, String subTitle, String imageUrl
            , LocalDateTime createAt, String createBy, LocalDateTime updateAt, String updateBy, String url, Integer status);

}
