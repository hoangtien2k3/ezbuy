package com.ezbuy.notificationservice.repository.repoTemplate.impl

import com.ezbuy.notificationmodel.dto.response.NotificationHeader
import com.ezbuy.notificationservice.repository.query.NotificationContentQuery
import com.ezbuy.notificationservice.repository.repoTemplate.NotificationContentRepoTemplate
import com.reactify.constants.CommonErrorCode
import com.reactify.exception.BusinessException
import com.reactify.model.response.DataResponse
import com.reactify.util.DataUtil
import com.reactify.util.SecurityUtils
import com.reactify.util.SortingUtils
import io.r2dbc.spi.Row
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
class NotificationContentRepoTemplateImpl(
    private val template: R2dbcEntityTemplate
) : NotificationContentRepoTemplate {

    override fun getNotificationContentListByCategoryType(
        categoryType: String,
        pageIndex: Int?,
        pageSize: Int?,
        sort: String?
    ): Mono<DataResponse<List<NotificationHeader>>> {
        if (pageIndex?.let { it < 1 } == true) {
            return Mono.error(
                BusinessException(
                    CommonErrorCode.INVALID_PARAMS,
                    "params.pageIndex.invalid"
                )
            )
        }
        if (pageSize?.let { it < 1 } == true) {
            return Mono.error(
                BusinessException(
                    CommonErrorCode.INVALID_PARAMS,
                    "params.pageSize.invalid"
                )
            )
        }
        return SecurityUtils.getCurrentUser().flatMap { user ->
            var sortingString = SortingUtils.parseSorting(sort, NotificationHeader::class.java)
            if (DataUtil.isNullOrEmpty(sortingString)) {
                sortingString = ""
            }
            val query = NotificationContentQuery.GET_NOTIFICATION_CONTENT_LIST_BY_CATEGORY_TYPE
                .trimIndent().format(sortingString)

            val index = ((pageIndex ?: 1) - 1) * (pageSize ?: 10)
            template.databaseClient.sql(query)
                .bind("receiver", user.id)
                .bind("categoryType", DataUtil.safeTrim(categoryType))
                .bind("pageSize", pageSize ?: 10)
                .bind("index", index)
                .map { row -> this.build(row as Row) }
                .all()
                .collectList()
                .flatMap { notificationContent ->
                    Mono.just(
                        DataResponse(
                            null,
                            CommonErrorCode.SUCCESS,
                            notificationContent
                        )
                    )
                }
                .switchIfEmpty(Mono.just(
                    DataResponse(
                        null,
                        CommonErrorCode.SUCCESS,
                        emptyList()
                    )
                ))
        }
    }

    private fun build(row: Row): NotificationHeader {
        return NotificationHeader.builder()
            .id(DataUtil.safeToString(row.get("id")))
            .title(DataUtil.safeToString(row.get("title")))
            .subTitle(DataUtil.safeToString(row.get("sub_title")))
            .imageUrl(DataUtil.safeToString(row.get("image_url")))
            .url(DataUtil.safeToString(row.get("url")))
            .status(DataUtil.safeToInt(row.get("status")))
            .createAt(row.get("create_at") as LocalDateTime)
            .createBy(DataUtil.safeToString(row.get("create_by")))
            .updateAt(row.get("update_at") as LocalDateTime)
            .updateBy(DataUtil.safeToString(row.get("update_by")))
            .state(DataUtil.safeToString(row.get("state")))
            .build()
    }
}