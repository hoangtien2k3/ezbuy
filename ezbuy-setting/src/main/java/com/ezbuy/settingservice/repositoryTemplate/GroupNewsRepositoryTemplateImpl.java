package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.GroupNewsDTO;
import com.ezbuy.settingservice.model.dto.request.SearchGroupNewsRequest;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class GroupNewsRepositoryTemplateImpl extends BaseTemplateRepository implements GroupNewsRepositoryTemplate {
    @Override
    public Flux<GroupNewsDTO> findGroupNews(SearchGroupNewsRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryGroupNews(query, params, request);
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " display_order \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), GroupNewsDTO.class);
        }
        query.append(" ORDER BY ").append(sorting).append(" \n").append(" LIMIT :pageSize  \n" + "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, GroupNewsDTO.class);
    }

    private void buildQueryGroupNews(
            StringBuilder builder, Map<String, Object> params, SearchGroupNewsRequest request) {
        builder.append(
                "select id, name, code, display_order, status, create_by, create_at, update_by, update_at from group_news where 1=1 ");
        if (!DataUtil.isNullOrEmpty(request.getId())) {
            builder.append(" and id = :id ");
            params.put("id", SQLUtils.replaceSpecialDigit(request.getId()));
        }
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            builder.append(" and code LIKE CONCAT('%',:code, '%') ");
            params.put("code", SQLUtils.replaceSpecialDigit(request.getCode().trim()));
        }
        if (!DataUtil.isNullOrEmpty(request.getName())) {
            builder.append(" and name LIKE CONCAT('%',:name, '%') ");
            params.put("name", SQLUtils.replaceSpecialDigit(request.getName().trim()));
        }
        if (request.getStatus() != null) {
            builder.append(" and status = :status");
            params.put("status", request.getStatus());
        }
        builder.append(" and (create_at BETWEEN  :fromDate AND  :toDate)  \n");
        params.put("fromDate", getFromDate(request.getFromDate()));
        params.put("toDate", getToDate(request.getToDate()));
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        return fromDate == null
                ? LocalDateTime.from(LocalDate.now().atStartOfDay().minusDays(30))
                : fromDate.atTime(0, 0, 0);
    }

    private LocalDateTime getToDate(LocalDate toDate) {
        return toDate == null ? LocalDateTime.from(LocalDate.now().atTime(LocalTime.MAX)) : toDate.atTime(23, 59, 59);
    }

    @Override
    public Mono<Long> countGroupNews(SearchGroupNewsRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryGroupNews(builder, params, request);
        return countQuery(builder.toString(), params);
    }
}
