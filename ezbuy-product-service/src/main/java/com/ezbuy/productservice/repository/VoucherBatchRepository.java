package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.VoucherBatch;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherBatchRepository extends R2dbcRepository<VoucherBatch, String> {

    Mono<VoucherBatch> findFirstById(String id);

    @Query("select * from voucher_batch order by create_at desc")
    Flux<VoucherBatch> getAllVoucherBatch();

    @Query("""
        SELECT * FROM voucher_batch
        JOIN voucher_type vt ON voucher_batch.voucher_type_id = vt.id
        WHERE voucher_batch.code = :code
        AND vt.id = :voucherTypeId
    """)
    Mono<VoucherBatch> getVBByCodeAndVouType(String code, String voucherTypeId);

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();

    @Modifying
    @Query(value = "update voucher_batch set status = :status where id = :id")
    Mono<Integer> updateStatus(String id, String status);

    @Query("select * from voucher_batch where  state =  'INPROGRESS' limit 1")
    Flux<VoucherBatch> findVoucherBatchInprogress();
}
