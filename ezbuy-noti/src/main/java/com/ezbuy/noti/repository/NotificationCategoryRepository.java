package com.ezbuy.noti.repository;

import com.ezbuy.noti.model.entity.NotificationCategory;
import com.ezbuy.noti.repository.query.NotificationCategoryQuery;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface NotificationCategoryRepository extends R2dbcRepository<NotificationCategory, String> {

    @Query(NotificationCategoryQuery.FIND_CATEGORY_ID_BY_TYPE)
    Mono<String> findCategoryIdByType(String categoryType);
}
