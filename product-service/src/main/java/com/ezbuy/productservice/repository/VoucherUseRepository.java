package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.VoucherUse;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherUseRepository extends R2dbcRepository<VoucherUse, UUID> {
    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();

    @Query("""
        SELECT * 
        FROM voucher_use vu 
        INNER JOIN voucher v ON v.id = vu.voucher_id  
        WHERE v.id = :voucherId 
          AND v.state = 'used' 
          AND vu.state = 'active' 
          AND (vu.expired_date >= NOW() OR vu.expired_date IS NULL) 
          AND vu.user_id = :userId
    """)
    Mono<VoucherUse> findVoucherUsedByVoucherIdAndUserId(String voucherId, String userId);

    @Query("select * from voucher_use where voucher_id = :voucherId")
    Mono<VoucherUse> findVoucherUseByVoucherId(String voucherId);

    @Query("select * from voucher_use where source_order_id = :orderId")
    Flux<VoucherUse> findVoucherUseByOrderId(String orderId);

    @Query("select * from voucher_use where source_order_id = :orderId and state = :state")
    Mono<VoucherUse> findFirstBySourceOrderIdAndState(String orderId, String state);

    @Query("select * from voucher_use where source_order_id = :orderId and state = :state")
    Flux<VoucherUse> findVoucherUseByOrderIdAndState(String orderId, String state);

    @Query("""
        SELECT vu.*
        FROM voucher_use vu 
        INNER JOIN voucher v ON vu.voucher_id = v.id 
        WHERE NOW() > DATE_ADD(vu.create_at, INTERVAL :minute MINUTE) 
          AND v.state = 'locked'
    """)
    Flux<VoucherUse> getAllExpiredVoucherUse(Integer minute);
    @Query("select * from voucher_use where voucher_id = :orderId and state = :state")
    Flux<VoucherUse> findVoucherUseByVoucherIdAndState(String orderId, String state);
}
