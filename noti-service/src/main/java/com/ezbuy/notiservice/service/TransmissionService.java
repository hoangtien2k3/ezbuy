/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.notiservice.service;

import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import com.ezbuy.notimodel.dto.response.NotificationHeader;
import com.ezbuy.notimodel.model.NotificationContent;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Service interface for handling transmission-related operations.
 */
public interface TransmissionService {

    /**
     * Retrieves the count of notice responses.
     *
     * @return a Mono emitting a DataResponse containing the count of notice
     *         responses
     */
    Mono<DataResponse> getCountNoticeResponseDTO();

    /**
     * Retrieves a list of notification headers by category type.
     *
     * @param type
     *            the category type
     * @param pageIndex
     *            the index of the page to retrieve
     * @param pageSize
     *            the size of the page to retrieve
     * @param sort
     *            the sorting criteria
     * @return a Mono emitting a DataResponse containing a list of
     *         NotificationHeader objects
     */
    Mono<DataResponse<List<NotificationHeader>>> getNotificationContentListByCategoryType(
            String type, Integer pageIndex, Integer pageSize, String sort);

    /**
     * Changes the transmission state by notification content ID and transmission
     * ID.
     *
     * @param state
     *            the new state
     * @param notificationContentId
     *            the ID of the notification content
     * @param transmissionId
     *            the ID of the transmission
     * @return a Mono emitting a DataResponse containing the result of the operation
     */
    Mono<DataResponse<Object>> changeTransmissionStateByIdAndReceiver(
            String state, String notificationContentId, String transmissionId);

    /**
     * Inserts a new transmission.
     *
     * @param createNotificationDTO
     *            the DTO containing the details of the notification to create
     * @return a Mono emitting a DataResponse containing the result of the operation
     */
    Mono<DataResponse<Object>> insertTransmission(CreateNotificationDTO createNotificationDTO);

    /**
     * Retrieves new notifications when online.
     *
     * @param newestNotiTime
     *            the time of the newest notification
     * @return a Mono emitting a DataResponse containing a list of
     *         NotificationContent objects
     */
    Mono<DataResponse<List<NotificationContent>>> getNewNotiWhenOnline(String newestNotiTime);
}
