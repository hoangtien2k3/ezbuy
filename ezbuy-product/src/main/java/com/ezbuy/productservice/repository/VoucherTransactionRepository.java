package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.VoucherTransaction;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherTransactionRepository extends R2dbcRepository<VoucherTransaction, UUID> {
    @Query("SELECT CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();

    @Query("""
            SELECT * 
            FROM voucher_transaction 
            WHERE voucher_id = :voucherId 
              AND state != :state
            """)
    Flux<VoucherTransaction> findVoucherTransByVoucherIdAndStateNotIn(String voucherId, String state);

    @Query("""
            SELECT * 
            FROM voucher_transaction 
            WHERE transaction_code = :orderId 
              AND state = :state
            """)
    Flux<VoucherTransaction> findVoucherTransByOrderIdAndState(String orderId, String state);

    @Query("""
            SELECT * 
            FROM voucher_transaction vt 
            WHERE NOW() > DATE_ADD(create_at, INTERVAL :minute MINUTE)
              AND state = 'preActive'
            """)
    Flux<VoucherTransaction> getAllExpiredVoucherTransaction(Integer minute);
}
