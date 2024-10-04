package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.model.MarketSection;
import com.ezbuy.settingmodel.request.MarketSectionSearchRequest;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.SortingUtils;
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
public class MarketSectionRepositoryTemplateImpl extends BaseRepositoryTemplate
        implements MarketSectionRepositoryTemplate {

    @Override
    public Flux<MarketSection> queryMarketSections(MarketSectionSearchRequest request) {
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " update_at DESC \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), MarketSection.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryPages(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n").append(" LIMIT :pageSize  \n" + "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, MarketSection.class);
    }

    @Override
    public Mono<Long> countMarketSections(MarketSectionSearchRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryPages(builder, params, request);

        return countQuery(builder.toString(), params);
    }

    private void buildQueryPages(
            StringBuilder builder, Map<String, Object> params, MarketSectionSearchRequest request) {
        builder.append("select * from market_section " + " where 1 = 1 ");
        if (!DataUtil.isNullOrEmpty(request.getType())) {
            builder.append(" and type = :type ");
            params.put("type", request.getType());
        }
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            builder.append(" and code like CONCAT('%', :code, '%') ");
            params.put("code", request.getCode());
        }
        if (!DataUtil.isNullOrEmpty(request.getName())) {
            builder.append(" and name like CONCAT('%', :name, '%') ");
            params.put("name", request.getName());
        }
        if (!Objects.isNull(request.getStatus())) {
            builder.append(" and status = :status ");
            params.put("status", request.getStatus());
        }
        if (!DataUtil.isNullOrEmpty(request.getFromDate())) {
            builder.append("and update_at >= :fromDate\n");
            params.put("fromDate", getFromDate(request.getFromDate()));
        }
        if (!DataUtil.isNullOrEmpty(request.getToDate())) {
            builder.append("and update_at <= :toDate\n");
            params.put("toDate", getToDate(request.getToDate()));
        }
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        return fromDate.atTime(0, 0, 0);
    }

    private LocalDateTime getToDate(LocalDate toDate) {
        return toDate.atTime(23, 59, 59);
    }
}
