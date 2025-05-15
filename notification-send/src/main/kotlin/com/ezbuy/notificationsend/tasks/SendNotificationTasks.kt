package com.ezbuy.notificationsend.tasks

import com.ezbuy.notificationsend.service.TransmissionService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class SendNotificationTasks(
    private val transmissionService: TransmissionService
) {

    private val log = LoggerFactory.getLogger(SendNotificationTasks::class.java)

    @Async
    @Scheduled(
        fixedRateString = "\${scheduled.fixedRate}",
        initialDelayString = "\${scheduled.initialDelay}"
    )
    fun sendNotificationTask() {
        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        log.info("=============================================")
        log.info("=====> STARTING JOB: SEND NOTIFICATION <=====")
        log.info("[START]\t\t Start: ${LocalDateTime.now().format(dateFormat)}")
        transmissionService.sendNotification()
            .doOnError { log.error("[ERROR] Error while sending notification: ${it.message}") }
            .doOnSuccess { log.info("[SUCCESS] Notification stream completed successfully") }
            .doFinally {
                log.info("=====> JOB FINISHED: SEND NOTIFICATION <=====")
                log.info("[END]\t\t   End: ${LocalDateTime.now().format(dateFormat)}")
                log.info("=============================================\n")
            }
            .subscribe()
    }
}