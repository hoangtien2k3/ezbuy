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
package com.ezbuy.authservice.client;

import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

/**
 * Client interface for interacting with the Notification Service.
 */
public interface NotiServiceClient {

    /**
     * Inserts a new transmission.
     *
     * @param createNotificationDTO
     *            the DTO containing the details of the notification to create
     * @return a Mono emitting an Optional containing a DataResponse if the
     *         operation was successful, or an empty Optional if not
     */
    Mono<Optional<DataResponse>> insertTransmission(CreateNotificationDTO createNotificationDTO);
}
