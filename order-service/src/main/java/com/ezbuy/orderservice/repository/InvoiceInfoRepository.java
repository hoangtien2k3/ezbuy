package com.ezbuy.orderservice.repository;

import com.ezbuy.ordermodel.model.InvoiceInfo;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface InvoiceInfoRepository extends R2dbcRepository<InvoiceInfo, UUID> {

    @Query(
            value =
                    "select * from invoice_info where user_id = :userId and organization_id = :organizationId and status = 1")
    Mono<InvoiceInfo> findByUserIdAndOrganizationId(String userId, String organizationId);

    @Query(value = "select * from invoice_info where id = :id")
    Mono<InvoiceInfo> getById(String id);

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
