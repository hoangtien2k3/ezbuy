package com.ezbuy.notificationservice.repository.repoTemplate

import com.ezbuy.notificationmodel.dto.response.NotificationHeader
import com.reactify.model.response.DataResponse
import reactor.core.publisher.Mono

interface NotificationContentRepoTemplate {
    fun getNotificationContentListByCategoryType(
        categoryType: String,
        pageIndex: Int?,
        pageSize: Int?,
        sort: String?
    ): Mono<DataResponse<List<NotificationHeader>>>
}