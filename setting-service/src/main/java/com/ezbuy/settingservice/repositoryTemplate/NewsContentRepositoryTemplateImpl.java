package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.ContentNewsDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.dto.request.QueryNewsRequest;
import com.ezbuy.settingmodel.dto.response.QueryNewsResponse;
import com.ezbuy.settingmodel.model.ContentNews;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.SQLUtils;
import io.hoangtien2k3.reactify.SortingUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class NewsContentRepositoryTemplateImpl implements NewsContentRepositoryTemplate {

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    private final ObjectMapper objectMapper;

    public NewsContentRepositoryTemplateImpl() {
        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();
    }

    @Override
    public Mono<QueryNewsResponse> queryNewsContent(QueryNewsRequest request) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQuery(request, query, params);
        return Mono.zip(
                        queryList(
                                        query.toString(),
                                        params,
                                        request.getPageIndex(),
                                        request.getPageSize(),
                                        ContentNewsDTO.class)
                                .collectList()
                                .defaultIfEmpty(new ArrayList<>()),
                        queryCount(query.toString(), params))
                .map(zip -> {
                    QueryNewsResponse response = new QueryNewsResponse();
                    response.setContent(zip.getT1());
                    response.setPagination(
                            new PaginationDTO(request.getPageIndex(), request.getPageSize(), zip.getT2()));
                    return response;
                });
    }

    private <T> Flux<T> queryList(
            String query, Map<String, Object> params, Integer pageIndex, Integer pageSize, Class<T> type) {
        StringBuilder builder = new StringBuilder(query)
                .append(" limit ")
                .append((pageIndex - 1) * pageSize)
                .append(",")
                .append(pageSize);
        DatabaseClient.GenericExecuteSpec spec =
                r2dbcEntityTemplate.getDatabaseClient().sql(builder.toString());
        if (!DataUtil.isNullOrEmpty(params)) {
            for (String param : params.keySet()) {
                spec = spec.bind(param, params.get(param));
            }
        }
        return spec.fetch().all().map(raw -> objectMapper.convertValue(raw, type));
    }

    private Mono<Long> queryCount(String query, Map<String, Object> params) {
        DatabaseClient.GenericExecuteSpec spec = r2dbcEntityTemplate
                .getDatabaseClient()
                .sql("select count(*) as count from (" + query + ") as common_count_alias");
        if (!DataUtil.isNullOrEmpty(params)) {
            for (String param : params.keySet()) {
                spec = spec.bind(param, params.get(param));
            }
        }
        return spec.fetch().first().map(raw -> raw.get("count")).cast(Long.class);
    }

    /**
     * build query string from query request
     *
     * @param request
     *            request params
     * @return
     */
    private void buildQuery(QueryNewsRequest request, StringBuilder builder, Map<String, Object> params) {
        builder.append("select * from content_news nc \n");
        builder.append("where 1 =1 \n");

        if (request.getFromDate() != null) {
            builder.append("and nc.update_at >= :from \n");
            params.put("from", request.getFromDate().atStartOfDay());
        }

        if (request.getToDate() != null) {
            builder.append("and nc.update_at < :toDate \n");
            params.put("toDate", request.getToDate().plusDays(1).atStartOfDay());
        }

        if (!DataUtil.isNullOrEmpty(request.getName())) {
            builder.append("and lower(nc.title) like concat('%', :name,'%') \n");
            params.put(
                    "name",
                    SQLUtils.replaceSpecialDigit(request.getName().trim().toLowerCase()));
        }

        if (!DataUtil.isNullOrEmpty(request.getSourceType())) {
            builder.append("and nc.source_type = :sourceType \n");
            params.put("sourceType", request.getSourceType());
        }

        if (!DataUtil.isNullOrEmpty(request.getNewsType())) {
            builder.append("and nc.news_type = :newsType \n");
            params.put("newsType", request.getNewsType());
        }

        if (request.getStatus() != null) {
            builder.append("and nc.status = :status \n");
            params.put("status", request.getStatus());
        }

        String sort = DataUtil.safeToString(request.getSort(), "-updateAt");
        String sortValue = SortingUtils.parseSorting(sort, ContentNews.class);
        builder.append(" order by ");
        if (!DataUtil.isNullOrEmpty(sortValue)) {
            builder.append(sortValue).append(" \n");
        } else {
            builder.append(" update_at DESC \n");
        }

        log.info(builder.toString());
    }
}
