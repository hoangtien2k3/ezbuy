package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.OptionSetValueDTO;
import com.ezbuy.settingmodel.dto.request.SearchOptionSetValueRequest;
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
