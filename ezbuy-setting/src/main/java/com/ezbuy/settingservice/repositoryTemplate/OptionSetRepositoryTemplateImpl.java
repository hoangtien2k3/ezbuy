package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.OptionSetDTO;
import com.ezbuy.settingmodel.dto.request.SearchOptionSetRequest;
import com.ezbuy.core.repository.BaseTemplateRepository;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SQLUtils;
import com.ezbuy.core.util.SortingUtils;
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
