package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.VoucherType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherTypeRepository extends R2dbcRepository<VoucherType, UUID> {

    @Query("SELECT * FROM voucher_type")
    Flux<VoucherType> getAllVoucherType();

    @Query("""
        SELECT vt.*
        FROM voucher_type vt
        INNER JOIN voucher v ON v.voucher_type_id = vt.id
        INNER JOIN voucher_use vu ON v.id = vu.voucher_id
        INNER JOIN voucher_transaction vs ON v.id = vs.voucher_id
        WHERE v.state = 'used'
          AND vu.state = 'active'
          AND vu.expired_date < NOW()
          AND vs.state = 'preActive'
          AND vs.user_id = :userId
          AND vu.source_order_id = :orderId
          AND vt.code IN (:codeList)
    """)
    Flux<VoucherType> validateVoucher(String userId, String orderId, List<String> codeList);

    @Query("select * from voucher_type where code = :code limit 1")
    Mono<VoucherType> findVoucherTypeByCode(String code);

    @Query("SELECT * FROM voucher_type where status = 1")
    Flux<VoucherType> getAllVoucherTypeActive();

    @Query(value = "select * from voucher_type order by create_at desc")
    Flux<VoucherType> findAllVoucherType();

    @Query(value = "select * from voucher_type where code = :code")
    Mono<VoucherType> findByCode(String code);

    @Query(value = "select * from voucher_type where id = :id")
    Mono<VoucherType> getById(String id);

    @Modifying
    @Query("""
        UPDATE voucher_type
        SET status = :status,
            update_by = :user,
            update_at = CURRENT_TIMESTAMP()
        WHERE id = :id
    """)
    Mono<VoucherType> updateStatus(String id, Integer status, String user);
}
