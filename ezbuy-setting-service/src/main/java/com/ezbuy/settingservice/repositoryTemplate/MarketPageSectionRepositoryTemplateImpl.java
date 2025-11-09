package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.request.SearchMarketPageSectionRequest;
import com.ezbuy.settingmodel.model.MarketPageSection;
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
public class MarketPageSectionRepositoryTemplateImpl extends BaseRepositoryTemplate
        implements MarketPageSectionRepositoryTemplate {

    @Override
    public Flux<MarketPageSection> findMarketPageSection(SearchMarketPageSectionRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryOptionSetValue(query, params, request);
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " update_at \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), MarketPageSection.class);
        }
        query.append(" ORDER BY ").append(sorting).append(" \n").append(" LIMIT :pageSize  \n" + " OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, MarketPageSection.class);
    }

    @Override
    public Mono<Long> countMarketPageSection(SearchMarketPageSectionRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryOptionSetValue(builder, params, request);
        return countQuery(builder.toString(), params);
    }

    private void buildQueryOptionSetValue(
            StringBuilder builder, Map<String, Object> params, SearchMarketPageSectionRequest request) {
        builder.append("select * from market_page_section where 1=1 ");
        if (!DataUtil.isNullOrEmpty(request.getPageId())) {
            builder.append(" and page_id = :pageId ");
            params.put("pageId", SQLUtils.replaceSpecialDigit(request.getPageId()));
        }
        if (request.getStatus() != null) {
            builder.append(" and status = :status");
            params.put("status", request.getStatus());
        }
    }
}
