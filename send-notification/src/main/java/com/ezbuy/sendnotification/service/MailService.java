package com.ezbuy.sendnotification.service;

import com.ezbuy.sendnotification.model.noti.TransmissionNotiDTO;
import com.ezbuy.sendnotification.model.noti.EmailResultDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MailService {
    Mono<List<EmailResultDTO>> sendMailByTransmission(List<TransmissionNotiDTO> transmissionNotis);
}