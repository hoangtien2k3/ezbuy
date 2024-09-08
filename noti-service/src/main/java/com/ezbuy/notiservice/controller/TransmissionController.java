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
package com.ezbuy.notiservice.controller;

import static com.ezbuy.notimodel.common.ConstValue.ControllerPath.TRANSMISSION_PATH;
import static com.ezbuy.notimodel.common.ConstValue.NotificationConstant.ANNOUNCEMENT;

import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import com.ezbuy.notimodel.dto.response.NotificationHeader;
import com.ezbuy.notimodel.model.NotificationContent;
import com.ezbuy.notiservice.service.TransmissionService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(TRANSMISSION_PATH)
public class TransmissionController {
    private final TransmissionService transmissionService;

    // @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/unread-noti")
    public Mono<DataResponse> getQuantityUserNewNoti() {
        return transmissionService.getCountNoticeResponseDTO();
    }

    // @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/noti")
    public Mono<DataResponse<List<NotificationHeader>>> getNotificationContentListByCategoryType(
            @Valid @RequestParam(required = false, defaultValue = ANNOUNCEMENT) String categoryType,
            @Valid @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
            @Valid @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @Valid @RequestParam(required = false, defaultValue = "-createAt") String sort) {
        return transmissionService.getNotificationContentListByCategoryType(categoryType, pageIndex, pageSize, sort);
    }

    // @PreAuthorize("hasAnyAuthority('user')")
    @PutMapping("/change-noti-state")
    public Mono<DataResponse<Object>> updateTransmissionState(
            @Valid @RequestParam(required = false) String state,
            @Valid @RequestParam(required = false) String notificationContentId,
            @Valid @RequestParam(required = false) String transmissionId) {
        return transmissionService.changeTransmissionStateByIdAndReceiver(state, notificationContentId, transmissionId);
    }

    // @PreAuthorize("hasAnyAuthority('admin','system')")
    @PostMapping("/create-noti")
    public Mono<DataResponse<Object>> insertTransmission(
            @Valid @RequestBody CreateNotificationDTO createNotificationDTO) {
        return transmissionService.insertTransmission(createNotificationDTO);
    }

    // @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/new-noti")
    public Mono<DataResponse<List<NotificationContent>>> getNewNotiWhenOnline(
            @RequestParam(required = false) @Size(message = "params.invalid.format", max = 50) String newestNotiTime) {
        return transmissionService.getNewNotiWhenOnline(newestNotiTime);
    }
}
