package com.ezbuy.orderservice.repository;

import com.ezbuy.ordermodel.model.InvoiceInfoHistory;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface InvoiceInfoHistoryRepository extends R2dbcRepository<InvoiceInfoHistory, UUID> {
    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
