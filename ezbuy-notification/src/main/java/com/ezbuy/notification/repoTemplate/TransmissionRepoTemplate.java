package com.ezbuy.notification.repoTemplate;

import com.ezbuy.notification.model.dto.UserTransmissionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface TransmissionRepoTemplate {

	Flux<UserTransmissionDTO> searchUserTransmission(String email, String templateMail, LocalDateTime from, LocalDateTime to, int offset, int limit, String sort);

	Mono<Long> countUserTransmission(String email, String templateMail, LocalDateTime from, LocalDateTime to);
}
