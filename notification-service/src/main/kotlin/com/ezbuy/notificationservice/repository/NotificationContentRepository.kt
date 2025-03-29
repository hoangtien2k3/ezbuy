package com.ezbuy.notificationservice.repository

import com.ezbuy.notificationservice.repository.query.NotificationContentQuery
import com.ezbuy.notificationmodel.model.NotificationContent
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface NotificationContentRepository : R2dbcRepository<NotificationContent, String> {

    override fun findById(id: String): Mono<NotificationContent>

    @Query(NotificationContentQuery.INSERT_NOTIFICATION_CONTENT)
    fun insert(
        id: String,
        title: String,
        subTitle: String,
        imageUrl: String,
        createAt: LocalDateTime,
        createBy: String,
        updateAt: LocalDateTime,
        updateBy: String,
        url: String,
        status: Int
    ): Mono<Boolean>
}
