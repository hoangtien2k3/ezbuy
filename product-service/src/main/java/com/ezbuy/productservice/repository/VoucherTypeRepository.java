package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.VoucherType;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface VoucherTypeRepository extends R2dbcRepository<VoucherType, UUID> {

  @Query("SELECT * FROM voucher_type")
  Flux<VoucherType> getAllVoucherType();

  @Query("select vt.*" +
          "from  voucher_type vt " +
          "         inner join voucher v on v.voucher_type_id = vt.id " +
          "         inner join voucher_use vu on v.id = vu.voucher_id " +
          "         inner join voucher_transaction vs on v.id = vs.voucher_id " +
          "where v.state = 'used' " +
          "  and vu.state = 'active' " +
          "  and vu.expired_date < NOW() " +
          "  and vs.state = 'preActive' " +
          "  and vs.user_id = :userId " +
          "  and vu.source_order_id = :orderId " +
          "  and vt.code in (:codeList)")
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
  @Query(value = "update voucher_type set status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
  Mono<VoucherType> updateStatus(String id, Integer status, String user);
}
