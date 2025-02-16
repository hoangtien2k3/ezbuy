package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.VoucherTransaction;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherTransactionRepository extends R2dbcRepository<VoucherTransaction, UUID> {
    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();

    @Query("select * from voucher_transaction where voucher_id = :voucherId and state != :state")
    Flux<VoucherTransaction> findVoucherTransByVoucherIdAndStateNotIn(String voucherId, String state);

    @Query("select * from voucher_transaction where transaction_code = :orderId and state = :state")
    Flux<VoucherTransaction> findVoucherTransByOrderIdAndState(String orderId, String state);

    @Query("select * " + "from voucher_transaction vt " + "where NOW() > DATE_ADD(create_at, INTERVAL :minute MINUTE) "
            + "  and state = 'preActive'")
    Flux<VoucherTransaction> getAllExpiredVoucherTransaction(Integer minute);
}
