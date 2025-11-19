package com.ezbuy.noti.controller;

import com.ezbuy.noti.common.ConstValue;
import com.ezbuy.noti.model.dto.UserTransmissionPageDTO;
import com.ezbuy.noti.model.dto.request.CreateNotificationDTO;
import com.ezbuy.noti.model.dto.request.GetTransmissionByEmailAndFromToRequest;
import com.ezbuy.noti.model.dto.response.CountNoticeResponseDTO;
import com.ezbuy.noti.model.dto.response.NotificationHeader;
import com.ezbuy.noti.model.entity.NotificationContent;
import com.ezbuy.noti.service.TransmissionService;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/noti/transmission")
public class TransmissionController {

    private final TransmissionService transmissionService;

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/unread-noti")
    public Mono<DataResponse<CountNoticeResponseDTO>> getQuantityUserNewNoti() {
        return transmissionService.getCountNoticeResponseDTO();
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/noti")
    public Mono<DataResponse<List<NotificationHeader>>> getNotificationContentListByCategoryType(
            @Valid @RequestParam(required = false, defaultValue = ConstValue.NotificationConstant.ANNOUNCEMENT) String categoryType,
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

    @PreAuthorize("hasAnyAuthority('admin','system','user')")
    @PostMapping("/create-noti")
    public Mono<DataResponse<Object>> insertTransmission(@Valid @RequestBody CreateNotificationDTO createNotificationDTO) {
        return transmissionService.insertTransmission(createNotificationDTO);
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/new-noti")
    public Mono<DataResponse<List<NotificationContent>>> getNewNotiWhenOnline(@RequestParam(required = false) @Size(message = "params.invalid.format", max = 50) String newestNotiTime) {
        return transmissionService.getNewNotiWhenOnline(newestNotiTime);
    }

    @GetMapping("/get-trans")
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<DataResponse<UserTransmissionPageDTO>> getUserTransmission(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @RequestParam(required = false, name = "template_mail") String templateMail,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "-create_at") String sort) {
        pageIndex = Math.max(pageIndex, 1);
        var request = GetTransmissionByEmailAndFromToRequest.builder()
                .email(DataUtil.safeTrim(email)).username(DataUtil.safeTrim(username))
                .templateMail(DataUtil.safeTrim(templateMail))
                .from(from).to(to)
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .limit(pageSize)
                .sort(sort)
                .build();
        return transmissionService.getTransmissionByEmailAndFromTo(request);
    }
}
