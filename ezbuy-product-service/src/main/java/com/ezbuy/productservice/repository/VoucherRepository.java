package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.dto.GetVoucherDTO;
import com.ezbuy.productmodel.model.Voucher;
import com.ezbuy.productmodel.model.VoucherType;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoucherRepository extends R2dbcRepository<Voucher, String> {
    Mono<Voucher> findFirstById(String id);

    @Query("""
            SELECT * FROM voucher
            JOIN voucher_type vt ON voucher.voucher_type_id = vt.id
            JOIN voucher_batch vb ON voucher.batch_id = vb.id
            WHERE voucher.code = :code
              AND vt.id = :voucherTypeId
              AND vb.id = :batchId
            """)
    Mono<Voucher> getVoucher(String code, String batchId, String voucherTypeId);

    @Query("""
            SELECT v.code, vt.condition_use, vt.description, vu.create_date, vu.expired_date, vt.code AS voucherTypeCode
            FROM voucher v
            JOIN voucher_use vu ON v.id = vu.voucher_id
            JOIN voucher_type vt ON vt.id = v.voucher_type_id
            WHERE vu.state = 'active'
              AND vu.user_id = :userId
              AND ('' IN (:voucherTypeCodeList) OR UPPER(vt.code) IN (:voucherTypeCodeListDuplicate))
              AND vt.status = 1
            """)
    Flux<GetVoucherDTO> getLstVoucherManager(
            String userId,
            List<String> voucherTypeCodeList,
            List<String> voucherTypeCodeListDuplicate
    );

    @Query("SELECT CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();

    @Query("SELECT * FROM voucher WHERE voucher_type_id = :voucherTypeId")
    Mono<Voucher> findVoucherByVoucherTypeId(String voucherTypeId);

    @Query("SELECT * FROM voucher WHERE id = :voucherId")
    Mono<Voucher> findVoucherByVoucherId(String voucherId);

    @Query("SELECT * FROM voucher_type WHERE status = 1")
    Flux<VoucherType> findAllVoucherTypeActive();

    @Query("""
            SELECT v.* FROM voucher_type vt
            INNER JOIN voucher v ON v.voucher_type_id = vt.id
            WHERE vt.code = :code
              AND v.state = 'new'
              AND vt.status = 1
              AND (v.expired_date > NOW() OR v.expired_date IS NULL)
            LIMIT 1 FOR UPDATE
            """)
    Mono<Voucher> findVoucherNewByVoucherTypeCode(String code);

    @Query("SELECT * FROM voucher WHERE id IN (:voucherIdList) AND state = :state")
    Flux<Voucher> findVoucherByVoucherIdListAndState(List<String> voucherIdList, String state);

    @Query("""
            SELECT * FROM voucher
            WHERE code = :code
              AND state = :state
              AND (expired_date >= NOW() OR expired_date IS NULL)
            """)
    Flux<Voucher> findVoucherByVoucherCodeAndState(String code, String state);

    @Query("SELECT * FROM voucher WHERE code = :code")
    Mono<Voucher> findVoucherByVoucherCode(String code);

    @Modifying
    @Query("""
            UPDATE voucher
            SET state = :state,
                update_at = :updateAt,
                update_by = :updateBy
            WHERE id = :id
              AND state = :prevState
            """)
    Mono<Long> updateVoucherStateByPreviousState(
            String state,
            LocalDateTime updateAt,
            String updateBy,
            String prevState,
            String id
    );

    @Query("""
            SELECT v.*
            FROM voucher_use vu
            INNER JOIN voucher v ON vu.voucher_id = v.id
            WHERE NOW() > DATE_ADD(vu.create_at, INTERVAL :minute MINUTE)
              AND v.state = 'locked'
              AND vu.state = 'preActive'
            """)
    Flux<Voucher> getAllExpiredVoucher(Integer minute);

    @Query("""
            SELECT * FROM voucher
            WHERE voucher_type_id = :voucherTypeId
              AND state = 'new'
            LIMIT 1
            """)
    Flux<Voucher> findVoucherUnused(String voucherTypeId);

    @Query("""
            SELECT v.* FROM voucher_type vt
            INNER JOIN voucher v ON v.voucher_type_id = vt.id
            WHERE vt.code = :code
              AND v.state = 'new'
              AND vt.status = 1
              AND (v.expired_date > NOW() OR v.expired_date IS NULL)
            LIMIT 1 FOR UPDATE
            """)
    Flux<Voucher> findAllVoucherNewByVoucherTypeCode(String code);

    @Modifying
    @Query("""
            UPDATE voucher
            SET state = :state,
                update_at = :updateAt,
                update_by = :updateBy
            WHERE id IN (:id)
              AND state = :prevState
            """)
    Mono<Long> updateAllVoucherStateByPreviousState(
            String state,
            LocalDateTime updateAt,
            String updateBy,
            String prevState,
            List<String> id
    );
}
