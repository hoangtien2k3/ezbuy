package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.VoucherUse;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface VoucherUseRepository extends R2dbcRepository<VoucherUse, UUID> {
    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();

    @Query("select * from voucher_use vu inner join voucher v on v.id = vu.voucher_id  where v.id = :voucherId and v.state = 'used' and vu.state = 'active' and (vu.expired_date >= NOW() || vu.expired_date IS NULL) and vu.user_id = :userId")
    Mono<VoucherUse> findVoucherUsedByVoucherIdAndUserId(String voucherId, String userId);

    @Query("select * from voucher_use where voucher_id = :voucherId")
    Mono<VoucherUse> findVoucherUseByVoucherId(String voucherId);

    @Query("select * from voucher_use where source_order_id = :orderId")
    Flux<VoucherUse> findVoucherUseByOrderId(String orderId);


    @Query("select * from voucher_use where source_order_id = :orderId and state = :state")
    Mono<VoucherUse> findFirstBySourceOrderIdAndState(String orderId, String state);

    @Query("select * from voucher_use where source_order_id = :orderId and state = :state")
    Flux<VoucherUse> findVoucherUseByOrderIdAndState(String orderId, String state);

    @Query("select vu.*  " +
            "from voucher_use vu " +
            "inner join voucher v on vu.voucher_id = v.id " +
            "where NOW() > DATE_ADD(vu.create_at, INTERVAL :minute MINUTE) " +
            "and v.state = 'locked'")
    Flux<VoucherUse> getAllExpiredVoucherUse(Integer minute);

    @Query("select * from voucher_use where voucher_id = :orderId and state = :state")
    Flux<VoucherUse> findVoucherUseByVoucherIdAndState(String orderId, String state);
}
