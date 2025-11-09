package com.ezbuy.productservice.repository.repoTemplate.impl;

import com.ezbuy.productmodel.dto.request.SearchProductRequest;
import com.ezbuy.productmodel.model.Product;
import com.ezbuy.productservice.repository.repoTemplate.BaseRepositoryTemplate;
import com.ezbuy.productservice.repository.repoTemplate.ProductCustomRepository;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SQLUtils;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl extends BaseRepositoryTemplate implements ProductCustomRepository {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<Product> searchProduct(SearchProductRequest request, String organizationId) {
        return handleSearchProduct(request, organizationId, false)
                .map((row, rowMetadata) -> buildEmployeeSearchResponse(row))
                .all();
    }

    @Override
    public Mono<Integer> countProduct(SearchProductRequest request, String organizationId) {
        return handleSearchProduct(request, organizationId, true)
                .map((row, rowMetadata) -> DataUtil.safeToInt(row.get("total"), 0))
                .one();
    }

    private Product buildEmployeeSearchResponse(Row row) {
        return Product.builder()
                .id(DataUtil.safeToString(row.get("id")))
                .code(DataUtil.safeToString(row.get("code")))
                .name(DataUtil.safeToString(row.get("name")))
                .priceImport(DataUtil.safeToDouble(row.get("price_import")))
                .priceExport(DataUtil.safeToDouble(row.get("price_export")))
                .unit(DataUtil.safeToString(row.get("unit")))
                .taxRatio(DataUtil.safeToString(row.get("tax_ratio")))
                .discount(DataUtil.safeToDouble(row.get("discount")))
                .revenueRatio(DataUtil.safeToLong(row.get("revenue_ratio")))
                .status(DataUtil.safeToInt(row.get("status")))
                .lockStatus(DataUtil.safeToInt(row.get("lock_status")))
                .createAt((LocalDateTime) row.get("create_at"))
                .updateAt((LocalDateTime) row.get("update_at"))
                .createBy(DataUtil.safeToString(row.get("create_by")))
                .updateBy(DataUtil.safeToString(row.get("update_by")))
                .createUnit(DataUtil.safeToString(row.get("create_unit")))
                .createUnit(DataUtil.safeToString(row.get("create_unit")))
                .organizationId(DataUtil.safeToString(row.get("organization_id")))
                .build();
    }

    private DatabaseClient.GenericExecuteSpec handleSearchProduct(
            SearchProductRequest request, String organizationId, boolean isCount) {
        StringBuilder query;
        if (!isCount) {
            query = new StringBuilder("select * from product where status = 1");
        } else {
            query = new StringBuilder("select count(id) as total from product where status = 1");
        }
        query.append(" and organization_id = :organizationId");
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            query.append(" and lower(code) like concat('%', :code, '%')");
        }
        if (!DataUtil.isNullOrEmpty(request.getName())) {
            query.append(" and lower(name) like concat('%', :name, '%')");
        }
        if (!DataUtil.isNullOrEmpty(request.getUnit())) {
            query.append(" and lower(unit) like concat('%', :unit, '%')");
        }
        if (!DataUtil.isNullOrEmpty(request.getLockStatus())) {
            query.append(" and lock_status = :lockStatus");
        }
        if (!isCount) {
            if (!DataUtil.isNullOrEmpty(request.getSort())) {
                query.append(" ORDER BY ");
                for (String str : request.getSort()) {
                    query.append(str.replace(";", " ")).append(",");
                }
                query.deleteCharAt(query.length() - 1);
            }
            if (request.getPageIndex() != null && request.getPageSize() != null) {
                query.append(" limit ")
                        .append((request.getPageIndex() - 1) * request.getPageSize())
                        .append(", ")
                        .append(request.getPageSize());
            }
        }
        DatabaseClient.GenericExecuteSpec executeSpec =
                template.getDatabaseClient().sql(query.toString());
        executeSpec = executeSpec.bind("organizationId", organizationId);
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            executeSpec = executeSpec.bind("code", request.getCode());
        }
        if (!DataUtil.isNullOrEmpty(request.getName())) {
            executeSpec = executeSpec.bind("name", request.getName());
        }
        if (!DataUtil.isNullOrEmpty(request.getUnit())) {
            executeSpec = executeSpec.bind("unit", request.getUnit());
        }
        if (!DataUtil.isNullOrEmpty(request.getLockStatus())) {
            executeSpec = executeSpec.bind("lockStatus", request.getLockStatus());
        }
        return executeSpec;
    }

    @Override
    public Flux<Product> getProductByIdAndOrganizationIdAndTransId(
            List<String> ids, UUID organizationId, Integer offset, Integer limit, String transactionId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("select p.* " + "from sme_product.product p "
                + "inner join sme_product.sync_history_detail d on p.id = d.target_id "
                + "inner join sme_product.sync_history s on d.sync_history_id = s.id " + "where 1 = 1 ");
        if (!DataUtil.isNullOrEmpty(ids)) {
            query.append(" and p.id in (:ids) ");
            params.put("ids", ids);
        }
        if (!DataUtil.isNullOrEmpty(organizationId)) {
            query.append(" and p.organization_id = :organizationId ");
            params.put("organizationId", SQLUtils.replaceSpecialDigit(String.valueOf(organizationId)));
        }
        if (!DataUtil.isNullOrEmpty(transactionId)) {
            query.append(" and s.sync_trans_id = :transactionId ");
            params.put("transactionId", SQLUtils.replaceSpecialDigit(transactionId));
        }
        query.append(" ORDER BY d.create_at desc ").append(" \n");
        if (offset != null && limit != null) {
            query.append(" LIMIT :pageSize  \n" + "OFFSET :index ");
            params.put("pageSize", limit);
            BigDecimal index = (new BigDecimal(offset)).multiply(new BigDecimal(limit));
            params.put("index", index);
        }
        return listQuery(query.toString(), params, Product.class);
    }

    protected <T> Flux<T> listQuery(String sql, Map<String, Object> params, Class<T> type) {
        DatabaseClient.GenericExecuteSpec spec = template.getDatabaseClient().sql(sql);
        if (!DataUtil.isNullOrEmpty(params)) {
            for (String param : params.keySet()) {
                spec = spec.bind(param, params.get(param));
            }
        }
        return spec.fetch().all().map(raw -> convert(raw, type));
    }
}
