package com.ezbuy.orderservice.repoTemplate;

import com.ezbuy.ordermodel.dto.response.OrderTransmissionDTO;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderTransactionRepositoryTemplate {
    Flux<OrderTransmissionDTO> searchOrderTransmission(
            String email,
            String orderCode,
            String idNo,
            String phone,
            LocalDateTime from,
            LocalDateTime to,
            int offset,
            int limit,
            String sort);

    Mono<Long> countOrderTransmission(
            String email, String orderCode, String idNo, String phone, LocalDateTime from, LocalDateTime to);
}
