package com.ezbuy.notificationsend.tasks

import com.ezbuy.notificationsend.service.TransmissionService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SendNotificationTasks(
    private val transmissionService: TransmissionService
) {

    private val log = LoggerFactory.getLogger(SendNotificationTasks::class.java)

    @Async
    @Scheduled(fixedRate = 30000, initialDelay = 5000)
    fun sendNotificationTask() {
        log.info("=========>Job send notification start<=========")
        transmissionService.sendNotification().subscribe()
        log.info("=========>Job send notification end<===========")
    }
}