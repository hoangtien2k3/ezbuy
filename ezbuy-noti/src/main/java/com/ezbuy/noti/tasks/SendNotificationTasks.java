package com.ezbuy.noti.tasks;

import com.ezbuy.noti.service.TransmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SendNotificationTasks {

    private final TransmissionService transmissionService;

    @Async
    @Scheduled(fixedRate = 30000, initialDelay = 5000)
    public void sendNotificationTask() {
        log.info("=========Job send notification start==========");
        transmissionService.sendNotification().subscribe();
        log.info("==========Job send notification end==========");
    }
}
