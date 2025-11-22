package com.ezbuy.paymentservice.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ezbuy.paymentservice.model.entity.RequestBanking;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RequestBankingRepository extends R2dbcRepository<RequestBanking, UUID> {
    @Query("""
            INSERT INTO request_banking (
                id, status, order_type, order_id, state,
                order_state, total_fee, merchant_code,
                create_at, create_by, update_at, update_by
            )
            VALUES (
                :id, 1, :orderType, :orderCode, :state,
                :orderState, :totalFee, :merchantCode,
                NOW(), :createBy, NOW(), :updateBy
            )
            """)
    Mono<RequestBanking> insertRequestBanking(
            String id,
            String orderType,
            String orderCode,
            Integer state,
            Integer orderState,
            Long totalFee,
            String merchantCode,
            String createBy,
            String updateBy);

    @Query("""
            SELECT * FROM request_banking
            WHERE id = :id AND status = 1
            """)
    Mono<RequestBanking> findRequestBankingByIdAndStatus(String id);

    @Query("""
            UPDATE request_banking
            SET vt_transaction_id = :vtTransactionId,
                state = :state,
                update_at = NOW(),
                update_by = 'system'
            WHERE id = :id
            """)
    Mono<Boolean> updateRequestBankingById(String id, String vtTransactionId, int state);

    @Query("""
            SELECT * FROM request_banking rb
            WHERE (:startTime IS NULL OR rb.create_at >= :startTime)
              AND (:endTime IS NULL OR rb.create_at < :endTime)
              AND rb.state = :state
              AND rb.status = 1
              AND (rb.update_state = 0 OR rb.update_state IS NULL)
            LIMIT :limit OFFSET :offset
            """)
    Flux<RequestBanking> findAllByStataAndTime(int state, LocalDateTime startTime, LocalDateTime endTime, int limit, long offset);
}
