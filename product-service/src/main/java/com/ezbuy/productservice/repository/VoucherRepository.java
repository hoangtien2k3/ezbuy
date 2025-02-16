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

    @Query("select * from voucher " + "JOIN voucher_type vt on voucher.voucher_type_id = vt.id "
            + "JOIN voucher_batch vb on voucher.batch_id = vb.id " + "where voucher.code=:code "
            + "and vt.id=:voucherTypeId " + "and vb.id=:batchId")
    Mono<Voucher> getVoucher(String code, String batchId, String voucherTypeId);

    @Query(
            "select v.code, vt.condition_use, vt.description, vu.create_date, vu.expired_date, vt.code as voucherTypeCode "
                    + "from voucher v " + "         JOIN voucher_use vu on v.id = vu.voucher_id "
                    + "         Join voucher_type vt on vt.id = v.voucher_type_id " + "where vu.state = 'active' "
                    + "  and vu.user_id = :userId "
                    + "  and (('') in (:voucherTypeCodeList) or UPPER(vt.code) in (:voucherTypeCodeListDuplicate)) "
                    + "  and vt.status = 1")
    Flux<GetVoucherDTO> getLstVoucherManager(
            String userId, List<String> voucherTypeCodeList, List<String> voucherTypeCodeListDuplicate);

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();

    @Query("select * from voucher where voucher_type_id = :voucherTypeId")
    Mono<Voucher> findVoucherByVoucherTypeId(String voucherTypeId);

    @Query("select * from voucher where id = :voucherId")
    Mono<Voucher> findVoucherByVoucherId(String voucherId);

    @Query("select * from voucher_type where status = 1")
    Flux<VoucherType> findAllVoucherTypeActive();

    @Query(
            "select v.* from voucher_type vt inner join voucher v on v.voucher_type_id = vt.id  where vt.code = :code and v.state = 'new' and vt.status = 1 and (v.expired_date > NOW() || v.expired_date IS NULL) limit 1 for update")
    Mono<Voucher> findVoucherNewByVoucherTypeCode(String code);

    @Query("select * from voucher where id in (:voucherIdList) and state = :state")
    Flux<Voucher> findVoucherByVoucherIdListAndState(List<String> voucherIdList, String state);

    @Query(
            "select * from voucher where code = :code and state = :state and (expired_date >= NOW() || expired_date IS NULL) ")
    Flux<Voucher> findVoucherByVoucherCodeAndState(String code, String state);

    @Query("select * from voucher where code = :code")
    Mono<Voucher> findVoucherByVoucherCode(String code);

    @Modifying
    @Query(
            value = "update voucher " + "set " + "state = :state, " + "update_at = :updateAt, "
                    + "update_by = :updateBy " + "where id = :id " + "and state = :prevState")
    Mono<Long> updateVoucherStateByPreviousState(
            String state, LocalDateTime updateAt, String updateBy, String prevState, String id);

    @Query("select v.*  " + "from voucher_use vu " + "inner join voucher v on vu.voucher_id = v.id "
            + "where NOW() > DATE_ADD(vu.create_at, INTERVAL :minute MINUTE) "
            + "and v.state = 'locked' and vu.state = 'preActive'")
    Flux<Voucher> getAllExpiredVoucher(Integer minute);

    @Query("select * from voucher where voucher_type_id = :voucherTypeId and state = 'new' limit 1")
    Flux<Voucher> findVoucherUnused(String voucherTypeId);

    @Query(
            "select v.* from voucher_type vt inner join voucher v on v.voucher_type_id = vt.id  where vt.code = :code and v.state = 'new' and vt.status = 1 and (v.expired_date > NOW() || v.expired_date IS NULL) limit 1 for update")
    Flux<Voucher> findAllVoucherNewByVoucherTypeCode(String code);

    @Modifying
    @Query(
            value = "update voucher " + "set " + "state = :state, " + "update_at = :updateAt, "
                    + "update_by = :updateBy " + "where id in (:id) " + "and state = :prevState")
    Mono<Long> updateAllVoucherStateByPreviousState(
            String state, LocalDateTime updateAt, String updateBy, String prevState, List<String> id);
}
