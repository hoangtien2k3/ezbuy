package com.ezbuy.notificationservice.service

import com.ezbuy.notificationmodel.dto.request.CreateNotificationDTO
import com.ezbuy.notificationmodel.dto.response.CountNoticeResponseDTO
import com.ezbuy.notificationmodel.dto.response.NotificationHeader
import com.ezbuy.notificationmodel.model.NotificationContent
import com.reactify.model.response.DataResponse
import reactor.core.publisher.Mono

interface TransmissionService {

    /**
     * Retrieves the count of notice responses.
     *
     * @return a Mono emitting a DataResponse containing the count of notice
     *         responses
     */
    fun getCountNoticeResponseDTO(): Mono<DataResponse<CountNoticeResponseDTO>>

    /**
     * Retrieves a list of notification headers by category type.
     *
     * @param type the category type
     * @param pageIndex the index of the page to retrieve
     * @param pageSize the size of the page to retrieve
     * @param sort the sorting criteria
     * @return a Mono emitting a DataResponse containing a list of
     *         NotificationHeader objects
     */
    fun getNotificationContentListByCategoryType(
        type: String,
        pageIndex: Int?,
        pageSize: Int?,
        sort: String?
    ): Mono<DataResponse<List<NotificationHeader>>>

    /**
     * Changes the transmission state by notification content ID and transmission
     * ID.
     *
     * @param state the new state
     * @param notificationContentId the ID of the notification content
     * @param transmissionId the ID of the transmission
     * @return a Mono emitting a DataResponse containing the result of the operation
     */
    fun changeTransmissionStateByIdAndReceiver(
        state: String,
        notificationContentId: String,
        transmissionId: String
    ): Mono<DataResponse<Any>>

    /**
     * Inserts a new transmission.
     *
     * @param createNotificationDTO the DTO containing the details of the notification to create
     * @return a Mono emitting a DataResponse containing the result of the operation
     */
    fun insertTransmission(createNotificationDTO: CreateNotificationDTO): Mono<DataResponse<Any>>

    /**
     * Retrieves new notifications when online.
     *
     * @param newestNotiTime the time of the newest notification
     * @return a Mono emitting a DataResponse containing a list of
     *         NotificationContent objects
     */
    fun getNewNotiWhenOnline(newestNotiTime: String): Mono<DataResponse<List<NotificationContent>>>
}
