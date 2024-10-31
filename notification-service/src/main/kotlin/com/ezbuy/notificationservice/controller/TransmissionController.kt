package com.ezbuy.notificationservice.controller

import com.ezbuy.notificationservice.service.TransmissionService
import com.ezbuy.notimodel.common.ConstValue.ControllerPath.TRANSMISSION_PATH
import com.ezbuy.notimodel.common.ConstValue.NotificationConstant.ANNOUNCEMENT
import com.ezbuy.notimodel.dto.request.CreateNotificationDTO
import com.ezbuy.notimodel.dto.response.CountNoticeResponseDTO
import com.ezbuy.notimodel.dto.response.NotificationHeader
import com.ezbuy.notimodel.model.NotificationContent
import io.hoangtien2k3.reactify.model.response.DataResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import lombok.RequiredArgsConstructor
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequiredArgsConstructor
@RequestMapping(TRANSMISSION_PATH)
class TransmissionController(
    private val transmissionService: TransmissionService
) {

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/unread-noti")
    fun getQuantityUserNewNoti(): Mono<DataResponse<CountNoticeResponseDTO>> {
        return transmissionService.getCountNoticeResponseDTO()
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/noti")
    fun getNotificationContentListByCategoryType(
        @Valid @RequestParam(required = false, defaultValue = ANNOUNCEMENT) categoryType: String,
        @Valid @RequestParam(required = false, defaultValue = "1") pageIndex: Int?,
        @Valid @RequestParam(required = false, defaultValue = "20") pageSize: Int?,
        @Valid @RequestParam(required = false, defaultValue = "-createAt") sort: String?
    ): Mono<DataResponse<List<NotificationHeader>>> {
        return transmissionService.getNotificationContentListByCategoryType(categoryType, pageIndex, pageSize, sort)
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @PutMapping("/change-noti-state")
    fun updateTransmissionState(
        @Valid @RequestParam(required = false) state: String,
        @Valid @RequestParam(required = false) notificationContentId: String,
        @Valid @RequestParam(required = false) transmissionId: String
    ): Mono<DataResponse<Any>> {
        return transmissionService.changeTransmissionStateByIdAndReceiver(state, notificationContentId, transmissionId)
    }

    @PreAuthorize("hasAnyAuthority('admin','system')")
    @PostMapping("/create-noti")
    fun insertTransmission(
        @Valid @RequestBody createNotificationDTO: CreateNotificationDTO
    ): Mono<DataResponse<Any>> {
        return transmissionService.insertTransmission(createNotificationDTO)
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/new-noti")
    fun getNewNotiWhenOnline(
        @RequestParam(required = false) @Size(message = "params.invalid.format", max = 50) newestNotiTime: String
    ): Mono<DataResponse<List<NotificationContent>>> {
        return transmissionService.getNewNotiWhenOnline(newestNotiTime)
    }
}
