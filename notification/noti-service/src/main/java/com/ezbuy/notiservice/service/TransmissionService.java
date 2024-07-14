package com.ezbuy.notiservice.service;

import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import com.ezbuy.notimodel.dto.response.NotificationHeader;
import com.ezbuy.notimodel.model.NotificationContent;
import com.viettel.sme.framework.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.List;


public interface TransmissionService {
    Mono<DataResponse> getCountNoticeResponseDTO();

    Mono<DataResponse<List<NotificationHeader>>> getNotificationContentListByCategoryType(String type, Integer pageIndex, Integer pageSize, String sort);

    Mono<DataResponse<Object>> changeTransmissionStateByIdAndReceiver(String state, String notificationContentId, String transmissionId);

    Mono<DataResponse<Object>> insertTransmission(CreateNotificationDTO createNotificationDTO);

    Mono<DataResponse<List<NotificationContent>>> getNewNotiWhenOnline(String newestNotiTime);
}
