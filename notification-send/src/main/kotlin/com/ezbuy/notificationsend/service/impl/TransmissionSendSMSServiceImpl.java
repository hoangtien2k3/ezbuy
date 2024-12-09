package com.ezbuy.notificationsend.service.impl;

import com.ezbuy.notificationmodel.common.ConstValue;
import com.ezbuy.notificationmodel.model.SendSms;
import com.ezbuy.notificationsend.repository.SendSmsRepository;
import com.ezbuy.notificationsend.service.SmsService;
import com.ezbuy.notificationsend.service.TransmissionSendSmsService;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransmissionSendSMSServiceImpl implements TransmissionSendSmsService {

    private final SendSmsRepository sendSmsRepo;
    private final SmsService smeService;

    @Value("${mode.send.backDay}")
    private Integer backendDay;

    @Value("${config.resendCount}")
    private Integer resendCount;
    @Value("${config.limit}")
    private Integer limit;

    @Override
    public Mono<DataResponse<Object>> sendSmsNotification() {
        return sendSmsRepo.findByStatusAlias(ConstValue.SendSmsStatus.NEW, limit, backendDay, true)
                .collectList()
                .flatMap(smsList -> {
                    if (smsList.isEmpty()) {
                        log.info("No new SMS to process.");
                        return Mono.just(new DataResponse<>(null, "no.new.sms.to.process", false));
                    }
                    // xu ly danh sach SMS
                    return processSmsList(smsList)
                            .thenReturn(new DataResponse<>(null, "sms.notification.processed.success", true));
                });
    }

    private Mono<Void> processSmsList(List<SendSms> smsList) {
        return Flux.fromIterable(smsList)
                .flatMap(this::processSmsWithUpdateStatus, getParallelism())
                .then();
    }

    private Mono<Void> processSmsWithUpdateStatus(SendSms sms) {
        return processSms(sms)
                .doOnSuccess(result -> updateSmsStatus(sms.getId(), ConstValue.SendSmsStatus.SENT, null, null))
                .doOnError(ex -> updateSmsStatus(sms.getId(), ConstValue.SendSmsStatus.SENT_FAILED, ex.getMessage(), null))
                .onErrorResume(ex -> {
                    log.error("Error processing SMS with ID: " + sms.getId(), ex);
                    return Mono.empty();
                });
    }

    private void updateSmsStatus(String smsId, Long status, String errorMessage, String exchangeId) {
        sendSmsRepo.updateResult(smsId, status, errorMessage, exchangeId)
                .doOnError(ex -> log.error("Failed to update status for SMS ID: " + smsId, ex))
                .subscribe();
    }

    private Mono<Void> processSms(SendSms sms) {
        // loai bo het dau trong noi dung tin nhan
        sms.setContent(sms.getContent());
        // thay so dien thoai nhan neu khong cho phep gui SMS
//        if (!allowSendSms) {
//            sms.setIsdn(testNumber);
//        }
        return Mono.defer(() -> {
//            String mode = modeSendSms;
//            if (isSendOnlineMode(mode) && !DataUtil.isNullOrEmpty(sms.getAlias())) {
//                return smeService.sendSmsBrandNameOnline(Collections.singletonList(sms))
//                        .then();
//            }
            return smeService.sendSmsBrandNameOnline(Collections.singletonList(sms)).then();
//            log.warn("Invalid SMS mode configuration for SMS ID: " + sms.getId());
//            return Mono.empty();
        }).onErrorResume(ex -> {
            log.error("Error occurred while sending SMS: " + sms.getId(), ex);
            return Mono.error(ex);
        });
    }

//    private boolean isSendOnlineMode(String mode) {
//        return ConstValue.OrdSysCfg.MODE_SEND_SMS_ONLINE.equals(mode) ||
//                ConstValue.OrdSysCfg.MODE_SEND_SMS_OFFLINE_ONLINE.equals(mode);
//    }

    private int getParallelism() {
        return Runtime.getRuntime().availableProcessors();
    }
}

