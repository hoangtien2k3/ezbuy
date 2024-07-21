package com.ezbuy.notification.service;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.notification.dto.request.CreateNotificationDTO;
import com.ezbuy.notification.dto.response.NotificationHeader;
import com.ezbuy.notification.model.NotificationContent;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransmissionService {
    Mono<DataResponse<Object>> insertTransmission(CreateNotificationDTO createNotificationDTO);

    Mono<DataResponse> getCountNoticeResponseDTO();

    Mono<DataResponse<List<NotificationContent>>> getNewNotiWhenOnline(String newestNotiTime);

    Mono<DataResponse<Object>> changeTransmissionStateByIdAndReceiver(String state, String notificationContentId, String transmissionId);

    Mono<DataResponse<List<NotificationHeader>>> getNotificationContentListByCategoryType(String type, Integer pageIndex, Integer pageSize, String sort);
}
