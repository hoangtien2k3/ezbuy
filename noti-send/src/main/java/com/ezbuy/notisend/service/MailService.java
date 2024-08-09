package com.ezbuy.notisend.service;

import com.ezbuy.notimodel.dto.EmailResultDTO;
import com.ezbuy.notimodel.dto.TransmissionNotiDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MailService {
    Mono<List<EmailResultDTO>> sendMailByTransmission(List<TransmissionNotiDTO> transmissionNotis);
}