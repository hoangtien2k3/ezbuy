package com.ezbuy.notiservice.service;

import com.ezbuy.notimodel.dto.UserTransmissionPageDTO;
import com.ezbuy.notimodel.dto.request.CreateNotificationDTO;
import com.ezbuy.notimodel.dto.request.GetTransmissionByEmailAndFromToRequest;
import com.ezbuy.notimodel.dto.response.CountNoticeResponseDTO;
import com.ezbuy.notimodel.dto.response.NotificationHeader;
import com.ezbuy.notimodel.model.NotificationContent;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransmissionService {
    Mono<DataResponse<CountNoticeResponseDTO>> getCountNoticeResponseDTO();

    Mono<DataResponse<List<NotificationHeader>>> getNotificationContentListByCategoryType(String type, Integer pageIndex, Integer pageSize, String sort);

    Mono<DataResponse<Object>> changeTransmissionStateByIdAndReceiver(String state, String notificationContentId, String transmissionId);

    Mono<DataResponse<Object>> insertTransmission(CreateNotificationDTO createNotificationDTO);

    Mono<DataResponse<List<NotificationContent>>> getNewNotiWhenOnline(String newestNotiTime);

    Mono<DataResponse<UserTransmissionPageDTO>> getTransmissionByEmailAndFromTo(GetTransmissionByEmailAndFromToRequest request);
}
