package com.ezbuy.notisend.service;

import com.ezbuy.notimodel.dto.EmailResultDTO;
import com.ezbuy.notimodel.dto.TransmissionNotiDTO;
import java.util.List;
import reactor.core.publisher.Mono;

public interface MailService {
    Mono<List<EmailResultDTO>> sendMailByTransmission(List<TransmissionNotiDTO> transmissionNotis);
}
