package com.ezbuy.notiservice.repository;

import com.ezbuy.notimodel.model.NotificationCategory;
import com.ezbuy.notiservice.repository.query.NotificationCategoryQuery;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface NotificationCategoryRepository extends R2dbcRepository<NotificationCategory, String> {
    Mono<NotificationCategory> findById(String id);

    @Query(NotificationCategoryQuery.findCategoryIdByType)
    Mono<String> findCategoryIdByType(String categoryType);
}
