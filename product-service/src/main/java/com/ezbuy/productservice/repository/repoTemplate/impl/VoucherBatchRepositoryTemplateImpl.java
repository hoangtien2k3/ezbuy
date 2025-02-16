package com.ezbuy.productservice.repository.repoTemplate.impl;

import com.ezbuy.productmodel.dto.request.VoucherBatchRequest;
import com.ezbuy.productmodel.model.VoucherBatch;
import com.ezbuy.productservice.repository.repoTemplate.BaseRepositoryTemplate;
import com.ezbuy.productservice.repository.repoTemplate.VoucherBatchRepositoryTemPlate;
import com.reactify.util.DataUtil;
import com.reactify.util.SortingUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class VoucherBatchRepositoryTemplateImpl extends BaseRepositoryTemplate
        implements VoucherBatchRepositoryTemPlate {

    @Override
    public Flux<VoucherBatch> queryVoucherBatch(VoucherBatchRequest request) {
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " update_at DESC \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), VoucherBatch.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryPages(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n").append(" LIMIT :pageSize  \n" + "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, VoucherBatch.class);
    }

    @Override
    public Mono<Long> countVoucherBatch(VoucherBatchRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryPages(builder, params, request);

        return countQuery(builder.toString(), params);
    }

    private void buildQueryPages(StringBuilder builder, Map<String, Object> params, VoucherBatchRequest request) {
        builder.append("select * from voucher_batch " + " where 1 = 1 ");
        if (!DataUtil.isNullOrEmpty(request.getId())) {
            builder.append(" and id = :id ");
            params.put("id", request.getId());
        }
        if (!DataUtil.isNullOrEmpty(request.getVoucherType())) {
            builder.append(" and voucher_type_id = :voucherType ");
            params.put("voucherType", request.getVoucherType());
        }
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            builder.append(" and code like CONCAT('%', :code, '%') ");
            params.put("code", request.getCode());
        }
        if (!DataUtil.isNullOrEmpty(request.getDescription())) {
            builder.append(" and description like CONCAT('%', :description, '%') ");
            params.put("description", request.getDescription());
        }
        if (!DataUtil.isNullOrEmpty(request.getQuantity())) {
            builder.append(" and quantity = :quantity ");
            params.put("quantity", request.getQuantity());
        }
        if (!DataUtil.isNullOrEmpty(request.getFromDate())) {
            builder.append("and create_at >= :fromDate\n");
            params.put("fromDate", getFromDate(request.getFromDate()));
        }
        if (!DataUtil.isNullOrEmpty(request.getToDate())) {
            builder.append("and create_at <= :toDate\n");
            params.put("toDate", getToDate(request.getToDate()));
        }
        if (!DataUtil.isNullOrEmpty(request.getFromExpiredDate())) {
            builder.append("and expired_date >= :fromExpiredDate\n");
            params.put("fromExpiredDate", getFromDate(request.getFromExpiredDate()));
        }
        if (!DataUtil.isNullOrEmpty(request.getToExpiredDate())) {
            builder.append("and expired_date <= :toExpiredDate\n");
            params.put("toExpiredDate", getToDate(request.getToExpiredDate()));
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
