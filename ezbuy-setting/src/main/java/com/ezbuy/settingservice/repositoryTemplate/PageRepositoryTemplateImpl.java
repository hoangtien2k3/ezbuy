package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.model.Page;
import com.ezbuy.settingmodel.request.SearchingPageRequest;
import com.ezbuy.core.repository.BaseTemplateRepository;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SQLUtils;
import com.ezbuy.core.util.SortingUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PageRepositoryTemplateImpl extends BaseTemplateRepository implements PageRepositoryTemplate {

    @Override
    public Flux<Page> queryPages(SearchingPageRequest request) {
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " create_at DESC \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), Page.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryPages(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n").append(" LIMIT :pageSize  \n" + "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, Page.class);
    }

    @Override
    public Mono<Long> countPages(SearchingPageRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryPages(builder, params, request);

        return countQuery(builder.toString(), params);
    }

    private void buildQueryPages(StringBuilder builder, Map<String, Object> params, SearchingPageRequest request) {
        builder.append("select * \n").append("from sme_setting.page u \n").append("where 1=1 \n");
        if (!DataUtil.isNullOrEmpty(request.getTitle())) {
            builder.append("and u.title like CONCAT('%',:title, '%') \n");
            params.put("title", SQLUtils.replaceSpecialDigit(request.getTitle()));
        }
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            builder.append("and u.code like concat('%', :code, '%') \n");
            params.put("code", SQLUtils.replaceSpecialDigit(request.getCode()));
        }
        if (!Objects.isNull(request.getStatus())) {
            builder.append("and u.status = :status \n");
            params.put("status", request.getStatus());
        }
        if (!DataUtil.isNullOrEmpty(request.getFromDate())) {
            builder.append("and u.update_at >= :fromDate \n");
            params.put("fromDate", getFromDate(request.getFromDate()));
        }
        if (!DataUtil.isNullOrEmpty(request.getToDate())) {
            builder.append("and u.update_at <= :toDate \n");
            params.put("toDate", getToDate(request.getToDate()));
        }
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        if (fromDate == null) {
            return LocalDateTime.from(LocalDate.now().atStartOfDay().minusDays(30));
        }
        return fromDate.atTime(0, 0, 0);
    }

    private LocalDateTime getToDate(LocalDate toDate) {
        if (toDate == null) {
            return LocalDateTime.from(LocalDate.now().atTime(LocalTime.MAX));
        }
        return toDate.atTime(23, 59, 59);
    }
}
