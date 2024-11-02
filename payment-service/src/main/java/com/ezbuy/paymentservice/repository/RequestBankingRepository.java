package com.ezbuy.paymentservice.repository;

import com.ezbuy.paymentmodel.model.RequestBanking;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RequestBankingRepository extends R2dbcRepository<RequestBanking, UUID> {

    @Query(
            value = "insert into `request_banking` (id, status, order_type, order_id, state, "
                    + "order_state, total_fee, merchant_code, create_at, create_by, update_at, update_by) "
                    + "values (:id, 1, :orderType, :orderCode, :state, "
                    + ":orderState, :totalFee, :merchantCode, NOW(), :createBy, NOW(), :updateBy)")
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

    @Query(value = "select * from `request_banking`" + "where id = :id and status = 1")
    Mono<RequestBanking> findRequestBankingByIdAndStatus(String id);

    @Query(
            value = "update `request_banking`"
                    + "set vt_transaction_id = :vtTransactionId, state = :state, update_at = NOW(), update_by = 'system'"
                    + "where id = :id")
    Mono<Boolean> updateRequestBankingById(String id, String vtTransactionId, int state);

    @Query(value = "select * from `request_banking` " + "where order_id = :orderCode and order_type = :orderType")
    Mono<RequestBanking> getRequestBankingByOrderCodeAndOrderType(String orderCode, String orderType);

    @Query(
            value = "select * from `request_banking` rb" + " where (:startTime is null or rb.create_at >= :startTime)"
                    + " and (:endTime is null or rb.create_at < :endTime)" + " and rb.state = :state"
                    + " and rb.status = 1"
                    + " and (rb.update_state = 0 or rb.update_state is null)" + " limit :limit offset :offset")
    Flux<RequestBanking> findAllByStataAndTime(
            int state, LocalDateTime startTime, LocalDateTime endTime, int limit, long offset);
}
