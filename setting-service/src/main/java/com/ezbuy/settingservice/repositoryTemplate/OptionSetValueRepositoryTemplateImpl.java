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

import com.ezbuy.settingmodel.dto.OptionSetValueDTO;
import com.ezbuy.settingmodel.dto.request.SearchOptionSetValueRequest;
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
public class OptionSetValueRepositoryTemplateImpl extends BaseTemplateRepository
        implements OptionSetValueRepositoryTemplate {

    @Override
    public Flux<OptionSetValueDTO> findOptionSetValue(SearchOptionSetValueRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryOptionSetValue(query, params, request);
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " code \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), OptionSetValueDTO.class);
        }
        query.append(" ORDER BY ").append(sorting).append(" \n").append(" LIMIT :pageSize  \n" + " OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, OptionSetValueDTO.class);
    }

    private void buildQueryOptionSetValue(
            StringBuilder builder, Map<String, Object> params, SearchOptionSetValueRequest request) {
        builder.append(
                "select id, option_set_id, code, value, description, status, create_by, create_at, update_by, update_at from option_set_value where 1=1 ");
        if (!DataUtil.isNullOrEmpty(request.getOptionSetId())) {
            builder.append(" and option_set_id = :optionSetId ");
            params.put("optionSetId", SQLUtils.replaceSpecialDigit(request.getOptionSetId()));
        }
        if (request.getStatus() != null) {
            builder.append(" and status = :status");
            params.put("status", request.getStatus());
        }
    }

    @Override
    public Mono<Long> countOptionSetValue(SearchOptionSetValueRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryOptionSetValue(builder, params, request);
        return countQuery(builder.toString(), params);
    }
}
