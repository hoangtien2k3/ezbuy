package com.ezbuy.notificationservice.repository

import com.ezbuy.notificationservice.repository.query.NotificationCategoryQuery
import com.ezbuy.notificationmodel.model.NotificationCategory
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

interface NotificationCategoryRepository : R2dbcRepository<NotificationCategory, String> {

    override fun findById(id: String): Mono<NotificationCategory>

    @Query(NotificationCategoryQuery.findCategoryIdByType)
    fun findCategoryIdByType(categoryType: String): Mono<String>
}
