package com.ezbuy.notification.controller;

import static com.ezbuy.notification.common.ConstValue.ControllerPath.TRANSMISSION_PATH;
import static com.ezbuy.notification.common.ConstValue.NotificationConstant.THONG_BAO;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.notification.dto.request.CreateNotificationDTO;
import com.ezbuy.notification.dto.response.NotificationHeader;
import com.ezbuy.notification.model.NotificationContent;
import com.ezbuy.notification.service.TransmissionService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(TRANSMISSION_PATH)
public class TransmissionController {
    private final TransmissionService transmissionService;

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/unread-noti")
    public Mono<DataResponse> getQuantityUserNewNoti() {
        return transmissionService.getCountNoticeResponseDTO();
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/noti")
    public Mono<DataResponse<List<NotificationHeader>>> getNotificationContentListByCategoryType(
            @Valid @RequestParam(required = false, defaultValue = THONG_BAO) String categoryType,
            @Valid @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
            @Valid @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @Valid @RequestParam(required = false, defaultValue = "-createAt") String sort) {
        return transmissionService.getNotificationContentListByCategoryType(categoryType, pageIndex, pageSize, sort);
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @PutMapping("/change-noti-state")
    public Mono<DataResponse<Object>> updateTransmissionState(
            @Valid @RequestParam(required = false) String state,
            @Valid @RequestParam(required = false) String notificationContentId,
            @Valid @RequestParam(required = false) String transmissionId) {
        return transmissionService.changeTransmissionStateByIdAndReceiver(state, notificationContentId, transmissionId);
    }

    //    @PreAuthorize("hasAnyAuthority('admin', 'system')")
    @PostMapping("/create-noti")
    public Mono<DataResponse<Object>> insertTransmission(
            @Valid @RequestBody CreateNotificationDTO createNotificationDTO) {
        return transmissionService.insertTransmission(createNotificationDTO);
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/new-noti")
    public Mono<DataResponse<List<NotificationContent>>> getNewNotiWhenOnline(
            @RequestParam(required = false) @Size(message = "params.invalid.format", max = 50) String newestNotiTime) {
        return transmissionService.getNewNotiWhenOnline(newestNotiTime);
    }
}
