package com.ezbuy.productservice.repository.repoTemplate.impl;

import com.ezbuy.productmodel.dto.VoucherTypeExtDTO;
import com.ezbuy.productmodel.dto.request.SearchVoucherRequest;
import com.ezbuy.productmodel.model.Voucher;
import com.ezbuy.productmodel.model.VoucherType;
import com.ezbuy.productservice.repository.repoTemplate.BaseRepositoryTemplate;
import com.ezbuy.productservice.repository.repoTemplate.VoucherRepositoryTemplate;
import com.reactify.util.DataUtil;
import com.reactify.util.SortingUtils;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class VoucherRepositoryTemplateImpl extends BaseRepositoryTemplate implements VoucherRepositoryTemplate {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<Voucher> queryVoucher(SearchVoucherRequest request) {
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " update_at DESC \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), Voucher.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryPages(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n")
                .append(" LIMIT :pageSize  \n" +
                        "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, Voucher.class);
    }

    @Override
    public Mono<Long> countVoucher(SearchVoucherRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryPages(builder, params, request);

        return countQuery(builder.toString(), params);
    }

    @Override
    public Flux<VoucherTypeExtDTO> validateVoucher(String userId, String extCode, List<String> voucherCodeList) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder("select vt.*, v.code as voucherCode " +
                " from  voucher_type vt " +
                " inner join voucher v on v.voucher_type_id = vt.id " +
                " inner join voucher_use vu on v.id = vu.voucher_id ");
        if (!DataUtil.isNullOrEmpty(extCode)) {
            query.append(" inner join voucher_transaction vs on v.id = vs.voucher_id ");
        }
        query.append(" where v.state = 'used' and vu.state = 'active' and (vu.expired_date >= NOW() || vu.expired_date IS NULL) ");
        if (!DataUtil.isNullOrEmpty(extCode)) {
            query.append(" and vs.state = 'preActive' and vs.user_id = :userId ");
            query.append(" and (vs.transaction_code = :orderId or :orderId = '') ");
        } else {
            query.append(" and vu.user_id = :userId ");
        }
        query.append(" and UPPER(v.code) in (:codeList)");
        DatabaseClient.GenericExecuteSpec executeSpec = template.getDatabaseClient().sql(query.toString());
        executeSpec = executeSpec.bind("userId", userId);
        if (!DataUtil.isNullOrEmpty(extCode)) {
            executeSpec = executeSpec.bind("orderId", extCode);
        }
        executeSpec = executeSpec.bind("codeList", voucherCodeList);

        params.put("userId", userId);
        params.put("orderId", extCode);
        params.put("codeList", voucherCodeList);
        return executeSpec.map((row, rowMetadata) -> buildValidateVoucherResponse(row)).all();
    }

    private VoucherTypeExtDTO buildValidateVoucherResponse(Row row) {
        VoucherType voucherType = VoucherType.builder()
                .id(DataUtil.safeToString(row.get("id")))
                .code(DataUtil.safeToString(row.get("code")))
                .name(DataUtil.safeToString(row.get("name")))
                .priorityLevel(DataUtil.safeToInt(row.get("priority_level")))
                .description(DataUtil.safeToString(row.get("description")))
                .actionType(DataUtil.safeToString(row.get("action_type")))
                .actionValue(DataUtil.safeToString(row.get("action_value")))
                .payment(DataUtil.safeToString(row.get("payment")))
                .state(DataUtil.safeToString(row.get("state")))
                .status(DataUtil.safeToInt(row.get("status")))
                .createAt((LocalDateTime) row.get("create_at"))
                .updateAt((LocalDateTime) row.get("update_at"))
                .createBy(DataUtil.safeToString(row.get("create_by")))
                .updateBy(DataUtil.safeToString(row.get("update_by")))
                .conditionUse(DataUtil.safeToString(row.get("condition_use")))
                .build();
        VoucherTypeExtDTO voucherTypeExtDTO = new VoucherTypeExtDTO(voucherType);
        voucherTypeExtDTO.setVoucherCode(DataUtil.safeToString(row.get("voucherCode")));
        return voucherTypeExtDTO;
    }

    private void buildQueryPages(StringBuilder builder, Map<String, Object> params, SearchVoucherRequest request) {
        builder.append("select * from voucher " +
                " where 1 = 1 ");
        if (!DataUtil.isNullOrEmpty(request.getId())) {
            builder.append(" and id = :id ");
            params.put("id", request.getId());
        }
        if (!DataUtil.isNullOrEmpty(request.getVoucherTypeId())) {
            builder.append(" and voucher_type_id = :voucherTypeId ");
            params.put("voucherTypeId", request.getVoucherTypeId());
        }
        if (!DataUtil.isNullOrEmpty(request.getBatchId())) {
            builder.append(" and batch_id = :batchId ");
            params.put("batchId", request.getBatchId());
        }
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            builder.append(" and code like CONCAT('%', :code, '%') ");
            params.put("code", request.getCode());
        }
        if (!DataUtil.isNullOrEmpty(request.getFromDate())) {
            builder.append("and create_at >= :fromDate\n");
            params.put("fromDate", getFromDate(request.getFromDate()));
        }
        if (!DataUtil.isNullOrEmpty(request.getToDate())) {
            builder.append("and create_at <= :toDate\n");
            params.put("toDate", getToDate(LocalDate.from(request.getToDate())));
        }
        if (!DataUtil.isNullOrEmpty(request.getFromExpiredDate())) {
            builder.append("and expired_date >= :fromExpiredDate\n");
            params.put("fromExpiredDate", request.getFromExpiredDate());
        }
        if (!DataUtil.isNullOrEmpty(request.getToExpiredDate())) {
            builder.append("and expired_date <= :toExpiredDate\n");
            params.put("toExpiredDate", request.getToExpiredDate());
        }
        if (!DataUtil.isNullOrEmpty(request.getState())) {
            builder.append(" and state = :state ");
            params.put("state", request.getState());
        }
        if (!DataUtil.isNullOrEmpty(request.getExpiredPeriod())) {
            builder.append(" and expired_period = :expiredPeriod ");
            params.put("expiredPeriod", request.getExpiredPeriod());
        }
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        return fromDate.atTime(0, 0, 0);
    }

    private LocalDateTime getToDate(LocalDate toDate) {
        return toDate.atTime(23, 59, 59);
    }

}
