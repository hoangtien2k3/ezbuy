package com.ezbuy.notificationsend.service;

import com.ezbuy.notificationmodel.dto.SmsResultDTO;
import com.ezbuy.notificationmodel.model.SendSms;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SmsService {

    Mono<List<SmsResultDTO>> sendSmsBrandNameOnline(List<SendSms> lstSendSms);

}