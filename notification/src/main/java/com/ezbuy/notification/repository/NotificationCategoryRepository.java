package com.ezbuy.notification.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ezbuy.notification.model.NotificationCategory;
import com.ezbuy.notification.repository.query.NotificationCategoryQuery;

import reactor.core.publisher.Mono;

public interface NotificationCategoryRepository extends R2dbcRepository<NotificationCategory, String> {
    Mono<NotificationCategory> findById(String id);

    @Query(NotificationCategoryQuery.findCategoryIdByType)
    Mono<String> findCategoryIdByType(String categoryType);
}
