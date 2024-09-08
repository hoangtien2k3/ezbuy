/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.authservice.repotemplate.impl;

import com.ezbuy.authmodel.dto.request.ActionLogRequest;
import com.ezbuy.authmodel.model.ActionLog;
import com.ezbuy.authservice.repotemplate.ActionLogRepositoryTemplate;
import io.hoangtien2k3.commons.repository.BaseTemplateRepository;
import io.hoangtien2k3.commons.utils.DataUtil;
import io.hoangtien2k3.commons.utils.SQLUtils;
import io.hoangtien2k3.commons.utils.SortingUtils;
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
            sorting = "create_at DESC \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), ActionLog.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQuery(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n");
        if (!DataUtil.isNullOrEmpty(request.getPageSize())) {
            query.append(" LIMIT :pageSize  \n" + "OFFSET :index ");
            params.put("pageSize", request.getPageSize());
            BigDecimal index =
                    (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
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
        builder.append("select * from action_log where 1=1 ");
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
        builder.append(" and (create_at BETWEEN :fromDate AND  :toDate)  \n");
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
