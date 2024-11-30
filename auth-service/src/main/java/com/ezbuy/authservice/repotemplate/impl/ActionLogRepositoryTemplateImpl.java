package com.ezbuy.authservice.repotemplate.impl;

import com.ezbuy.authmodel.dto.request.ActionLogRequest;
import com.ezbuy.authmodel.model.ActionLog;
import com.ezbuy.authservice.repotemplate.ActionLogRepositoryTemplate;
import com.reactify.repository.BaseTemplateRepository;
import com.reactify.util.DataUtil;
import com.reactify.util.SQLUtils;
import com.reactify.util.SortingUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ActionLogRepositoryTemplateImpl extends BaseTemplateRepository implements ActionLogRepositoryTemplate {

    @Override
    public Flux<ActionLog> search(ActionLogRequest request) {
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = "create_at DESC";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), ActionLog.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQuery(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n");
        if (!DataUtil.isNullOrEmpty(request.getPageSize())) {
            query.append("LIMIT :pageSize \n");
            query.append("OFFSET :index \n");
            params.put("pageSize", request.getPageSize());
            BigDecimal index = (new BigDecimal(request.getPageIndex() - 1))
                    .multiply(new BigDecimal(request.getPageSize()));
            params.put("index", index);
        }
        return listQuery(query.toString(), params, ActionLog.class);
    }

    @Override
    public Mono<Long> count(ActionLogRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQuery(builder, params, request);
        return countQuery(builder.toString(), params);
    }

    private void buildQuery(StringBuilder builder, Map<String, Object> params, ActionLogRequest request) {
        builder.append("select * from action_log where 1 = 1 ");
        if (!DataUtil.isNullOrEmpty(request.getUsername())) {
            builder.append(" and username LIKE CONCAT('%',:username, '%') ");
            params.put(
                    "username",
                    SQLUtils.replaceSpecialDigit(request.getUsername().trim()));
        }
        if (!DataUtil.isNullOrEmpty(request.getIp())) {
            builder.append(" and ip LIKE CONCAT('%',:ip, '%') ");
            params.put("ip", SQLUtils.replaceSpecialDigit(request.getIp().trim()));
        }
        if (!DataUtil.isNullOrEmpty(request.getType())) {
            builder.append(" and type LIKE CONCAT('%', :type, '%') ");
            params.put("type", SQLUtils.replaceSpecialDigit(request.getType().trim()));
        }
        builder.append(" and (create_at BETWEEN :fromDate AND :toDate)  \n");
        params.put("fromDate", getFromDate(request.getFromDate()));
        params.put("toDate", getToDate(request.getToDate()));
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        return fromDate == null
                ? LocalDateTime.from(LocalDate.now().atStartOfDay().minusDays(32))
                : fromDate.atTime(0, 0, 0);
    }

    private LocalDateTime getToDate(LocalDate toDate) {
        return toDate == null ? LocalDateTime.from(LocalDate.now().atTime(LocalTime.MAX)) : toDate.atTime(23, 59, 59);
    }
}
