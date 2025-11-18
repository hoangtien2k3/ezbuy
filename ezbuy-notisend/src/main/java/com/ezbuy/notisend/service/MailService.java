package com.ezbuy.notisend.service;

import com.ezbuy.notisend.model.dto.EmailResultDTO;
import com.ezbuy.notisend.model.dto.TransmissionNotiDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MailService {

    Mono<List<EmailResultDTO>> sendMailByTransmission(List<TransmissionNotiDTO> transmissionNotis);
}