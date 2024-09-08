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
package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.OptionSetDTO;
import com.ezbuy.settingmodel.dto.request.SearchOptionSetRequest;
import io.hoangtien2k3.commons.repository.BaseTemplateRepository;
import io.hoangtien2k3.commons.utils.DataUtil;
import io.hoangtien2k3.commons.utils.SQLUtils;
import io.hoangtien2k3.commons.utils.SortingUtils;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class OptionSetRepositoryTemplateImpl extends BaseTemplateRepository implements OptionSetRepositoryTemplate {

    @Override
    public Flux<OptionSetDTO> findOptionSet(SearchOptionSetRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryOptionSet(query, params, request);
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " code \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), OptionSetDTO.class);
        }
        query.append(" ORDER BY ").append(sorting).append(" \n").append(" LIMIT :pageSize  \n" + " OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, OptionSetDTO.class);
    }

    private void buildQueryOptionSet(
            StringBuilder builder, Map<String, Object> params, SearchOptionSetRequest request) {
        builder.append(
                "select id, code, description, status, create_by, create_at, update_by, update_at from option_set where 1=1 ");
        if (!DataUtil.isNullOrEmpty(request.getId())) {
            builder.append(" and id = :id ");
            params.put("id", SQLUtils.replaceSpecialDigit(request.getId()));
        }
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            builder.append(" and code LIKE CONCAT('%',:code, '%') ");
            params.put("code", SQLUtils.replaceSpecialDigit(request.getCode().trim()));
        }
        if (!DataUtil.isNullOrEmpty(request.getDescription())) {
            builder.append(" and description LIKE CONCAT('%',:description, '%') ");
            params.put(
                    "description",
                    SQLUtils.replaceSpecialDigit(request.getDescription().trim()));
        }
        if (request.getStatus() != null) {
            builder.append(" and status = :status");
            params.put("status", request.getStatus());
        }
        if (request.getFromDate() != null) {
            builder.append(" and create_at >= :fromDate ");
            params.put("fromDate", request.getFromDate().atStartOfDay());
        }
        if (request.getToDate() != null) {
            builder.append(" and create_at <= :toDate  ");
            params.put("toDate", request.getToDate().plusDays(1).atStartOfDay());
        }
    }

    @Override
    public Mono<Long> countOptionSet(SearchOptionSetRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryOptionSet(builder, params, request);
        return countQuery(builder.toString(), params);
    }
}
