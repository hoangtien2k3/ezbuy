package com.ezbuy.noti.service;

import com.ezbuy.noti.model.dto.EmailResultDTO;
import com.ezbuy.noti.model.dto.TransmissionNotiDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MailService {

    Mono<List<EmailResultDTO>> sendMailByTransmission(List<TransmissionNotiDTO> transmissionNotis);
}