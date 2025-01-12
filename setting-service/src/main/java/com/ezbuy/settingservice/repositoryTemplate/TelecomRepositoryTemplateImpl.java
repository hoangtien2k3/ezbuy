package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.TelecomDTO;
import com.ezbuy.settingmodel.model.Telecom;
import com.ezbuy.settingmodel.request.PageTelecomRequest;
import com.ezbuy.settingmodel.request.TelecomSearchingRequest;
import com.reactify.repository.BaseTemplateRepository;
import com.reactify.util.DataUtil;
import com.reactify.util.SQLUtils;
import com.reactify.util.SortingUtils;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelecomRepositoryTemplateImpl extends BaseTemplateRepository implements TelecomRepositoryTemplate {

    private final R2dbcEntityTemplate template;

    public Flux<TelecomDTO> getAll(List<String> ids, List<String> aliases, List<String> origins) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from telecom_service where status = 1 ");
        if (!DataUtil.isNullOrEmpty(ids)) {
            sb.append("and id in (:ids)");
        }
        if (!DataUtil.isNullOrEmpty(aliases)) {
            sb.append("and service_alias in (:aliases)");
        }
        if (!DataUtil.isNullOrEmpty(origins)) {
            sb.append("and origin_id in (:origins)");
        }
        DatabaseClient.GenericExecuteSpec genericExecuteSpec =
                template.getDatabaseClient().sql(sb.toString());
        if (!DataUtil.isNullOrEmpty(ids)) {
            genericExecuteSpec = genericExecuteSpec.bind("ids", ids);
        }
        if (!DataUtil.isNullOrEmpty(aliases)) {
            genericExecuteSpec = genericExecuteSpec.bind("aliases", aliases);
        }
        if (!DataUtil.isNullOrEmpty(origins)) {
            genericExecuteSpec = genericExecuteSpec.bind("origins", origins);
        }
        return genericExecuteSpec.map((a, b) -> build(a)).all();
    }

    @Override
    public Flux<Telecom> queryTelecomServices(TelecomSearchingRequest request) {
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " name \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), Telecom.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryTelecomServices(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n").append(" LIMIT :pageSize  \n" + "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, Telecom.class);
    }

    @Override
    public Mono<Long> countTelecomServices(TelecomSearchingRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryTelecomServices(builder, params, request);

        return countQuery(builder.toString(), params);
    }

    private void buildQueryTelecomServices(
            StringBuilder builder, Map<String, Object> params, TelecomSearchingRequest request) {
        builder.append("SELECT * \n")
                .append("FROM telecom_service u \n")
                .append("WHERE 1 = 1 AND u.is_filter = 1 \n");
        if (!DataUtil.isNullOrEmpty(request.getName())) {
            builder.append("AND LOWER(u.name) LIKE '%' || LOWER(:name) || '%' \n");
            params.put("name", SQLUtils.replaceSpecialDigit(request.getName()));
        }
    }

    @Override
    public Flux<TelecomDTO> getAllByRequest(PageTelecomRequest request) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        Integer pageIndex = DataUtil.safeToInt(request.getPageIndex(), 1);
        pageIndex = pageIndex >= 1 ? pageIndex : 1;
        Integer pageSize = DataUtil.safeToInt(request.getPageSize(), 10);
        pageSize = pageSize < 1 ? pageSize : 10;

        buildListQuery(request, sb, params);

        sb.append(" limit :limit offset :offset ");
        params.put("limit", pageSize);
        params.put("offset", (pageSize * (pageIndex - 1)));

        return listQuery(sb.toString(), params, TelecomDTO.class);
    }

    @Override
    public Mono<Long> getTotalByRequest(PageTelecomRequest request) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        buildListQuery(request, sb, params);

        return countQuery(sb.toString(), params);
    }

    private void buildListQuery(PageTelecomRequest request, StringBuilder sb, Map<String, Object> params) {
        sb.append("select * from telecom_service where 1 = 1 ");

        if (request.getServiceTypeId() != null) {
            sb.append(" AND origin_id = :originId  ");
            params.put("originId", request.getServiceTypeId());
        }
        if (!DataUtil.isNullOrEmpty(request.getServiceName())) {
            sb.append(" AND name like concat('%', :name, '%') ");
            params.put("name", replaceSpecialDigit(request.getServiceName()));
        }
        if (request.getStatus() != null) {
            sb.append(" AND status like :status ");
            params.put("status", request.getStatus());
        }
        if (!DataUtil.isNullOrEmpty(request.getServiceType())) {
            sb.append(" AND service_alias like :serviceType");
            params.put("serviceType", request.getServiceType());
        }

        String sort = DataUtil.safeToString(request.getSort(), "-createAt");
        String sortValue = SortingUtils.parseSorting(sort, Telecom.class);
        sb.append(" order by ");
        if (!DataUtil.isNullOrEmpty(sortValue)) {
            sb.append(sortValue).append(" \n");
        } else {
            sb.append(" service_alias, `name` \n");
        }
    }

    private TelecomDTO build(Row row) {
        return TelecomDTO.builder()
                .id(DataUtil.safeToString(row.get("id")))
                .name(DataUtil.safeToString(row.get("name")))
                .description(DataUtil.safeToString(row.get("description")))
                .serviceAlias(DataUtil.safeToString(row.get("service_alias")))
                .image(DataUtil.safeToString(row.get("image")))
                .status(DataUtil.safeToInt(row.get("status")))
                .originId(DataUtil.safeToString(row.get("origin_id")))
                .isFilter(DataUtil.safeToBoolean(row.get("is_filter")))
                .connectOne(DataUtil.safeToBoolean(row.get("connect_one")))
                .deployOrderCode(DataUtil.safeToString(row.get("deploy_order_code")))
                .build();
    }

    @Override
    public Flux<String> getServiceTypes() {
        String query = "select distinct service_alias from telecom_service order by service_alias";
        return template.getDatabaseClient()
                .sql(query)
                .fetch()
                .all()
                .map(raw -> raw.get("service_alias"))
                .cast(String.class);
    }

    private String replaceSpecialDigit(String input) {
        if (DataUtil.isNullOrEmpty(input)) {
            return "";
        }
        return input.replace("%", "\\%").replace("_", "\\_");
    }
}
