package com.ezbuy.noti.service;

import com.ezbuy.noti.model.dto.UserTransmissionPageDTO;
import com.ezbuy.noti.model.dto.request.CreateNotificationDTO;
import com.ezbuy.noti.model.dto.request.GetTransmissionByEmailAndFromToRequest;
import com.ezbuy.noti.model.dto.response.CountNoticeResponseDTO;
import com.ezbuy.noti.model.dto.response.NotificationHeader;
import com.ezbuy.noti.model.entity.NotificationContent;
import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransmissionService {

    Mono<DataResponse<Object>> sendNotification();

    Mono<DataResponse<CountNoticeResponseDTO>> getCountNoticeResponseDTO();

    Mono<DataResponse<List<NotificationHeader>>> getNotificationContentListByCategoryType(String type, Integer pageIndex, Integer pageSize, String sort);

    Mono<DataResponse<Object>> changeTransmissionStateByIdAndReceiver(String state, String notificationContentId, String transmissionId);

    Mono<DataResponse<Object>> insertTransmission(CreateNotificationDTO createNotificationDTO);

    Mono<DataResponse<List<NotificationContent>>> getNewNotiWhenOnline(String newestNotiTime);

    Mono<DataResponse<UserTransmissionPageDTO>> getTransmissionByEmailAndFromTo(GetTransmissionByEmailAndFromToRequest request);
}
