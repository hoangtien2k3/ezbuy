package com.ezbuy.notificationsend.tasks;

import com.ezbuy.notificationsend.service.TransmissionSendSmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SendSmsTasks {

    private final TransmissionSendSmsService transmissionSendSmsService;

    @Async
    @Scheduled(fixedRate = 30000, initialDelay = 5000)
    public void sendNotificationTask() {
        log.info("=========Job send sms notification start==========");
        transmissionSendSmsService.sendSmsNotification().subscribe();
        log.info("==========Job send sms notification end==========");
    }

}
