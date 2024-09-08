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

import com.ezbuy.settingmodel.dto.MarketPageDTO;
import com.ezbuy.settingmodel.model.MarketPage;
import com.ezbuy.settingmodel.request.SearchMarketPageRequest;
import io.hoangtien2k3.commons.utils.DataUtil;
import io.hoangtien2k3.commons.utils.SortingUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MarketPageRepositoryTemplateImpl extends BaseRepositoryTemplate implements MarketPageRepositoryTemplate {

    @Override
    public Flux<MarketPageDTO> queryList(SearchMarketPageRequest request) {
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " update_at DESC \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), MarketPage.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQuery(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n").append(" LIMIT :pageSize  \n" + "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);
        return listQuery(query.toString(), params, MarketPageDTO.class);
    }

    @Override
    public Mono<Long> count(SearchMarketPageRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQuery(builder, params, request);
        return countQuery(builder.toString(), params);
    }

    private void buildQuery(StringBuilder builder, Map<String, Object> params, SearchMarketPageRequest request) {

        builder.append("select  market_page.*,ts.name as name_service  from market_page \n");
        builder.append("left join telecom_service ts on ts.origin_id = market_page.service_id  \n");
        builder.append("where 1 = 1 \n");
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            builder.append(" and market_page.code like CONCAT('%', :code, '%') ");
            params.put("code", request.getCode());
        }
        if (!DataUtil.isNullOrEmpty(request.getName())) {
            builder.append(" and market_page.name like CONCAT('%', :name, '%') ");
            params.put("name", request.getName());
        }
        if (!Objects.isNull(request.getStatus())) {
            builder.append(" and market_page.status = :status ");
            params.put("status", request.getStatus());
        }
        if (!DataUtil.isNullOrEmpty(request.getFromDate())) {
            builder.append("and market_page.create_at >= :fromDate\n");
            params.put("fromDate", getFromDate(request.getFromDate()));
        }
        if (!DataUtil.isNullOrEmpty(request.getToDate())) {
            builder.append("and market_page.create_at <= :toDate\n");
            params.put("toDate", getToDate(request.getToDate()));
        }
        if (!DataUtil.isNullOrEmpty(request.getServiceId())) {
            builder.append("and market_page.service_id = :serviceId \n");
            params.put("serviceId", request.getServiceId());
        }

        // bo sung filter theo serviceAlias PYCXXX/LuongToanTrinhScontract
        if (!DataUtil.isNullOrEmpty(request.getServiceAlias())) {
            builder.append("and market_page.service_alias = :serviceAlias \n");
            params.put("serviceAlias", request.getServiceAlias());
        }
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        return fromDate.atTime(0, 0, 0);
    }

    private LocalDateTime getToDate(LocalDate toDate) {
        return toDate.atTime(23, 59, 59);
    }
}
