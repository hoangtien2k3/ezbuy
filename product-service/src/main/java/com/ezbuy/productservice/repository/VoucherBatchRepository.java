package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.VoucherBatch;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface VoucherBatchRepository extends R2dbcRepository<VoucherBatch, String> {

  Mono<VoucherBatch> findFirstById(String id);
  @Query("select * from voucher_batch order by create_at desc")
  Flux<VoucherBatch> getAllVoucherBatch();

  @Query("select * from voucher_batch\n"
      + "JOIN voucher_type vt on voucher_batch.voucher_type_id = vt.id\n"
      + "where voucher_batch.code=:code\n"
      + "and vt.id=:voucherTypeId")
  Mono<VoucherBatch> getVBByCodeAndVouType(String code, String voucherTypeId);

  @Query("select CURRENT_TIMESTAMP")
  Mono<LocalDateTime> getSysDate();
  @Modifying
  @Query(value="update voucher_batch set status = :status where id = :id")
  Mono<Integer> updateStatus(String id, String status);
  @Query("select * from voucher_batch where  state =  'INPROGRESS' limit 1")
  Flux<VoucherBatch> findVoucherBatchInprogress();
}
