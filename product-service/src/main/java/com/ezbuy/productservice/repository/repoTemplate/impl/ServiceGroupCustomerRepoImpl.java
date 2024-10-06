package com.ezbuy.productservice.repository.repoTemplate.impl;

import com.ezbuy.productmodel.dto.ServiceDTO;
import com.ezbuy.productmodel.dto.ServiceGroupDTO;
import com.ezbuy.productmodel.request.SearchServiceGroupRequest;
import com.ezbuy.productservice.repository.repoTemplate.BaseRepositoryTemplate;
import com.ezbuy.productservice.repository.repoTemplate.ServiceGroupCustomerRepo;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.SQLUtils;
import io.hoangtien2k3.reactify.SortingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ServiceGroupCustomerRepoImpl extends BaseRepositoryTemplate implements ServiceGroupCustomerRepo {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<ServiceGroupDTO> findServiceGroup(SearchServiceGroupRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryServiceGroups(query, params, request);
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " display_order \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), ServiceGroupDTO.class);
        }
        query.append(" ORDER BY ").append(sorting).append(" \n")
                .append(" LIMIT :pageSize  \n" +
                        "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, ServiceGroupDTO.class);
    }

    private void buildQueryServiceGroups(StringBuilder builder, Map<String, Object> params, SearchServiceGroupRequest request) {
        builder.append("select id, name, display_order, status, create_by, create_at, update_by, update_at, code from service_group where 1=1 ");
        if (!DataUtil.isNullOrEmpty(request.getId())) {
            builder.append(" and id = :groupId ");
            params.put("groupId", SQLUtils.replaceSpecialDigit(request.getId()));
        }
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            builder.append(" and code LIKE CONCAT('%',:code, '%') ");
            params.put("code", SQLUtils.replaceSpecialDigit(request.getCode()));
        }
        if (!DataUtil.isNullOrEmpty(request.getName())) {
            builder.append(" and name LIKE CONCAT('%',:name, '%') ");
            params.put("name", SQLUtils.replaceSpecialDigit(request.getName()));
        }
        if (request.getStatus() != null) {
            builder.append(" and status = :status");
            params.put("status", request.getStatus());
        }
        builder.append(" and (create_at BETWEEN  :fromDate AND  :toDate)  \n");
        params.put("fromDate", getFromDate(request.getFromDate()));
        params.put("toDate", getToDate(request.getToDate()));
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        return fromDate == null ? LocalDateTime.from(LocalDate.now().atStartOfDay().minusDays(30)) : fromDate.atTime(0, 0, 0);
    }

    private LocalDateTime getToDate(LocalDate toDate) {
        return toDate == null ? LocalDateTime.from(LocalDate.now().atTime(LocalTime.MAX)) : toDate.atTime(23, 59, 59);
    }

    @Override
    public Mono<Long> countServiceGroup(SearchServiceGroupRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryServiceGroups(builder, params, request);
        return countQuery(builder.toString(), params);
    }

    @Override
    public Flux<ServiceDTO> getAllServiceGroupAndTelecomServiceActive() {
        return template.getDatabaseClient()
                .sql("select sg.id as groupId, sg.name as groupName, sg.display_order as displayOrder, ts.origin_id as originId, ts.service_alias as serviceAlias\n" + //bo sung alias
                        "from service_group sg\n" +
                        "left join telecom_service ts on sg.id = ts.group_id\n" +
                        "where sg.status = 1 and ts.status = 1")
                .map(row -> ServiceDTO.builder()
                        .groupId(DataUtil.safeToString(row.get("groupId")))
                        .groupName(DataUtil.safeToString(row.get("groupName")))
                        .order(DataUtil.safeToInt(row.get("displayOrder")))
                        .originId(DataUtil.safeToString(row.get("originId")))
                        .telecomServiceAlias(DataUtil.safeToString(row.get("serviceAlias"))) // bos sung alias
                        .build()).all();
    }
}
