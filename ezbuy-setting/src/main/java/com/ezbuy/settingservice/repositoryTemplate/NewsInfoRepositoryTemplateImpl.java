package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.GroupNewsDTO;
import com.ezbuy.settingservice.model.dto.NewsDetailDTO;
import com.ezbuy.settingservice.model.dto.NewsInfoDTO;
import com.ezbuy.settingservice.model.dto.RelateNewsDTO;
import com.ezbuy.settingservice.model.dto.request.SearchNewsInfoRequest;
import com.ezbuy.core.repository.BaseTemplateRepository;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SQLUtils;
import com.ezbuy.core.util.SortingUtils;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class NewsInfoRepositoryTemplateImpl extends BaseTemplateRepository implements NewsInfoRepositoryTemplate {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<NewsInfoDTO> findNewsInfo(SearchNewsInfoRequest request) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQueryNewsInfo(query, params, request);
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " display_order \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), GroupNewsDTO.class);
        }
        query.append(" ORDER BY ").append(sorting);
        if (request.getIsWeb() != null && request.getIsWeb()) {
            query.append(" , ni.display_order asc  ").append(" \n");
        } else {
            query.append(" \n");
        }
        query.append(" LIMIT :pageSize  \n" + "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        return listQuery(query.toString(), params, NewsInfoDTO.class);
    }

    private void buildQueryNewsInfo(StringBuilder builder, Map<String, Object> params, SearchNewsInfoRequest request) {
        if (request.getIsWeb() != null && request.getIsWeb()) {
            builder.append(
                    "select ni.id, ni.title, ni.code, ni.display_order, ni.status, ni.summary, ni.state, ni.group_news_id, "
                            + "ni.create_by, ni.create_at, ni.update_by, ni.update_at, ni.navigator_url, "
                            + "gn.name as group_news_name, gn.code as group_news_code, gn.display_order as group_news_order "
                            + "from news_info ni " + "left join group_news gn on ni.group_news_id = gn.id "
                            + "where gn.status = 1 ");
        } else {
            builder.append(
                    "select ni.id, ni.title, ni.code, ni.display_order, ni.status, ni.summary, ni.state, ni.group_news_id, "
                            + "ni.create_by, ni.create_at, ni.update_by, ni.update_at, ni.navigator_url, gn.name as group_news_name, gn.code as group_news_code from news_info ni "
                            + "left join group_news gn on ni.group_news_id = gn.id " + "where 1=1 ");
        }
        if (!DataUtil.isNullOrEmpty(request.getId())) {
            builder.append(" and ni.id = :id ");
            params.put("id", SQLUtils.replaceSpecialDigit(request.getId()));
        }
        if (!DataUtil.isNullOrEmpty(request.getGroupNewsId())) {
            builder.append(" and ni.group_news_id = :groupNewsId ");
            params.put("groupNewsId", SQLUtils.replaceSpecialDigit(request.getGroupNewsId()));
        }
        if (!DataUtil.isNullOrEmpty(request.getCode())) {
            builder.append(" and ni.code LIKE CONCAT('%',:code, '%') ");
            params.put("code", SQLUtils.replaceSpecialDigit(request.getCode().trim()));
        }
        if (!DataUtil.isNullOrEmpty(request.getTitle())) {
            builder.append(" and ni.title LIKE CONCAT('%',:title, '%') ");
            params.put("title", SQLUtils.replaceSpecialDigit(request.getTitle().trim()));
        }
        if (!DataUtil.isNullOrEmpty(request.getSummary())) {
            builder.append(" and ni.summary LIKE CONCAT('%',:summary, '%') ");
            params.put(
                    "summary", SQLUtils.replaceSpecialDigit(request.getSummary().trim()));
        }
        if (!DataUtil.isNullOrEmpty(request.getState())) {
            builder.append(" and ni.state LIKE CONCAT('%',:state, '%') ");
            params.put("state", SQLUtils.replaceSpecialDigit(request.getState().trim()));
        }
        if (request.getStatus() != null) {
            builder.append(" and ni.status = :status");
            params.put("status", request.getStatus());
        }
        builder.append(" and (ni.create_at BETWEEN  :fromDate AND  :toDate)  \n");
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
    public Mono<Long> countNewsInfo(SearchNewsInfoRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryNewsInfo(builder, params, request);
        return countQuery(builder.toString(), params);
    }

    @Override
    public Flux<NewsDetailDTO> getNewsDetailByNewsInfoId(String id) {
        StringBuilder query = new StringBuilder();
        query.append("select ni.title, gn.id, gn.name, ni.create_at as createAt, nc.content " + "from news_info ni "
                + "left join group_news gn on ni.group_news_id = gn.id and gn.status = 1 "
                + "left join news_content nc on nc.news_info_id = ni.id and nc.status = 1 " + "where ni.status = 1 "
                + "and ni.id = :id");
        DatabaseClient.GenericExecuteSpec genericExecuteSpec =
                template.getDatabaseClient().sql(query.toString());
        genericExecuteSpec = genericExecuteSpec.bind("id", id);
        return genericExecuteSpec.map((a, b) -> build(a)).all();
    }

    private NewsDetailDTO build(Row row) {
        return NewsDetailDTO.builder()
                .title(DataUtil.safeToString(row.get("title")))
                .groupId(DataUtil.safeToString(row.get("id")))
                .groupName(DataUtil.safeToString(row.get("name")))
                .createAt((LocalDateTime) row.get("createAt"))
                .content(DataUtil.safeToString(row.get("content")))
                .build();
    }

    @Override
    public Flux<RelateNewsDTO> getRelateNewsByGroupNewsId(String id) {
        StringBuilder query = new StringBuilder();
        query.append(
                "select ni.id, ni.navigator_url as navigatorUrl, gn.name, ni.create_at as createAt, ni.title, ni.summary "
                        + "from news_info ni "
                        + "left join group_news gn on ni.group_news_id = gn.id and gn.status = 1 "
                        + "where ni.status = 1 and ni.group_news_id = :id " + "order by ni.create_at desc "
                        + "limit 3");
        DatabaseClient.GenericExecuteSpec genericExecuteSpec =
                template.getDatabaseClient().sql(query.toString());
        genericExecuteSpec = genericExecuteSpec.bind("id", id);
        return genericExecuteSpec.map((row, metadata) -> buildRelateNews(row)).all();
    }

    private RelateNewsDTO buildRelateNews(Row row) {
        return RelateNewsDTO.builder()
                .id(DataUtil.safeToString(row.get("id")))
                .navigatorUrl(DataUtil.safeToString(row.get("navigatorUrl")))
                .groupName(DataUtil.safeToString(row.get("name")))
                .createAt((LocalDateTime) row.get("createAt"))
                .summary(DataUtil.safeToString(row.get("summary")))
                .title(DataUtil.safeToString(row.get("title")))
                .build();
    }
}
