package com.ezbuy.productservice.repository.repoTemplate.impl;

import com.ezbuy.productmodel.dto.VoucherTypeDTO;
import com.ezbuy.productmodel.dto.VoucherTypeV2DTO;
import com.ezbuy.productmodel.dto.request.SearchVoucherTypeRequest;
import com.ezbuy.productmodel.model.VoucherType;
import com.ezbuy.productservice.repository.repoTemplate.BaseRepositoryTemplate;
import com.ezbuy.productservice.repository.repoTemplate.VoucherTypeRepositoryTemplate;
import com.reactify.util.DataUtil;
import com.reactify.util.SortingUtils;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class VoucherTypeRepositoryTemplateImpl extends BaseRepositoryTemplate implements VoucherTypeRepositoryTemplate {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<VoucherTypeDTO> search(SearchVoucherTypeRequest request) {
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = "update_at DESC, create_at DESC ";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), VoucherType.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQuery(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" ").append(" LIMIT :pageSize  " + "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);
        return listQuery(query.toString(), params, VoucherTypeDTO.class);
    }

    /**
     * Ham nay dung de xay dung cau truy van dua tren yeu cau
     *
     * @param builder
     * @param params
     * @param request
     */
    private void buildQuery(StringBuilder builder, Map<String, Object> params, SearchVoucherTypeRequest request) {
        builder.append("select * from sme_product.voucher_type where 1=1 ");
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            builder.append(" and voucher_type.code like CONCAT('%', :code, '%') ");
            params.put("code", request.getCode());
        }
        if (!DataUtil.isNullOrEmpty(request.getName())) {
            builder.append(" and voucher_type.name like CONCAT('%', :name, '%') ");
            params.put("name", request.getName());
        }
        if (!DataUtil.isNullOrEmpty(request.getDescription())) {
            builder.append(" and voucher_type.description like CONCAT('%', :description, '%') ");
            params.put("description", request.getDescription());
        }
        if (!DataUtil.isNullOrEmpty(request.getState())) {
            builder.append(" and voucher_type.description like CONCAT('%', :state, '%') ");
            params.put("state", request.getState());
        }
        if (!DataUtil.isNullOrEmpty(request.getStatus())) {
            builder.append(" and status = :status ");
            params.put("status", request.getStatus());
        }
        if (!DataUtil.isNullOrEmpty(request.getPriorityLevel())) {
            builder.append(" and priority_level = :priority_level ");
            params.put("priority_level", request.getPriorityLevel());
        }
        if (!DataUtil.isNullOrEmpty(request.getActionType())) {
            builder.append(" and action_type like CONCAT('%', :action_type, '%') ");
            params.put("action_type", request.getActionType());
        }

        if (!DataUtil.isNullOrEmpty(request.getCreateFromDate())) {
            builder.append(" and create_at >= :fromDate");
            params.put("fromDate", getFromDate(request.getCreateFromDate()));
        }
        if (!DataUtil.isNullOrEmpty(request.getCreateToDate())) {
            builder.append(" and create_at <= :toDate ");
            params.put(" toDate ", getToDate(request.getCreateToDate()));
        }
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        return fromDate == null
                ? LocalDateTime.from(LocalDate.now().atStartOfDay().minusDays(30))
                : fromDate.atTime(0, 0, 0);
    }

    private LocalDateTime getToDate(LocalDate toDate) {
        return toDate == null ? LocalDateTime.from(LocalDate.now().atTime(LocalTime.MAX)) : toDate.atTime(23, 59, 59);
    }

    @Override
    public Mono<Long> count(SearchVoucherTypeRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQuery(builder, params, request);
        return countQuery(builder.toString(), params);
    }

    @Override
    public Flux<VoucherTypeV2DTO> findVoucherTypeByVoucherCodeUsed(String code) {
        StringBuilder builder = new StringBuilder();
        builder.append("select vt.*, v.code as voucherCode ");
        builder.append("from voucher_type vt ");
        builder.append("inner join voucher v on vt.id = v.voucher_type_id ");
        builder.append("where vt.status = 1 ");
        builder.append("and v.code = :code ");
        builder.append("and UPPER(v.state) = 'USED' ");
        DatabaseClient.GenericExecuteSpec executeSpec =
                template.getDatabaseClient().sql(builder.toString());
        executeSpec = executeSpec.bind("code", code);
        return executeSpec
                .map((row, rowMetaData) -> buildFindVoucherTypeByVoucherCodeUsedResponse(row))
                .all();
    }

    private VoucherTypeV2DTO buildFindVoucherTypeByVoucherCodeUsedResponse(Row row) {
        VoucherTypeV2DTO voucherType = new VoucherTypeV2DTO();
        voucherType.setId(DataUtil.safeToString(row.get("id")));
        voucherType.setCode(DataUtil.safeToString(row.get("code")));
        voucherType.setVoucherCode(DataUtil.safeToString(row.get("voucherCode")));
        return voucherType;
    }
}
