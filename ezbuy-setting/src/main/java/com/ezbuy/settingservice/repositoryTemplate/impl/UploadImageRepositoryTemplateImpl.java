package com.ezbuy.settingservice.repositoryTemplate.impl;

import com.ezbuy.settingservice.model.dto.UploadImagesDTO;
import com.ezbuy.settingservice.model.dto.request.SearchImageRequest;
import com.ezbuy.settingservice.model.entity.UploadImages;
import com.ezbuy.settingservice.repositoryTemplate.UploadImageRepositoryTemplate;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SQLUtils;
import com.ezbuy.core.util.SortingUtils;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UploadImageRepositoryTemplateImpl implements UploadImageRepositoryTemplate {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<UploadImagesDTO> queryList(SearchImageRequest request) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQuery(query, params, request);
        query.append(" LIMIT :pageSize  \n" + "OFFSET :index ");
        params.put("pageSize", request.getPageSize());
        BigDecimal index = (new BigDecimal(request.getPageIndex() - 1)).multiply(new BigDecimal(request.getPageSize()));
        params.put("index", index);

        DatabaseClient.GenericExecuteSpec exeSpec = template.getDatabaseClient().sql(query.toString());
        for (String key : params.keySet()) {
            exeSpec = exeSpec.bind(key, params.get(key));
        }
        return exeSpec.map((row, metadata) -> convertRow(row)).all();
    }

    @Override
    public Mono<Long> count(SearchImageRequest request) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQuery(query, params, request);
        query.insert(0, "select count(1) from (");
        query.append(") repo_count_alias");
        DatabaseClient.GenericExecuteSpec exeSpec = template.getDatabaseClient().sql(query.toString());
        for (String key : params.keySet()) {
            exeSpec = exeSpec.bind(key, params.get(key));
        }
        return exeSpec.map(row -> row.get(0)).first().cast(Long.class);
    }

    private void buildQuery(StringBuilder builder, Map<String, Object> params, SearchImageRequest request) {
        builder.append(
                """
        SELECT u.*, COUNT(c.id) AS total_images,\s
        STRING_AGG(c.path, ',' ORDER BY c.update_at DESC) AS preview_images\s
        FROM upload_images u\s
        """);
        builder.append("LEFT JOIN upload_images c ON c.parent_id = u.id AND c.status = 1 \n");
        builder.append("WHERE u.status = 1 \n");
        if (!DataUtil.isNullOrEmpty(request.getFromDate())) {
            builder.append("AND u.update_at >= :from \n");
            params.put("from", request.getFromDate().atStartOfDay());
        }
        if (!DataUtil.isNullOrEmpty(request.getToDate())) {
            builder.append("AND u.update_at < :to \n");
            params.put("to", request.getToDate().plusDays(1).atStartOfDay());
        }
        if (!DataUtil.isNullOrEmpty(request.getName())) {
            builder.append("AND LOWER(u.name) LIKE '%' || :name || '%' \n");
            params.put(
                    "name",
                    SQLUtils.replaceSpecialDigit(request.getName().trim().toLowerCase()));
        }
        builder.append(
                """
        GROUP BY u.id, u.name, u.type, u.path, u.parent_id, u.status,\s
        u.create_at, u.create_by, u.update_at, u.update_by\s
        """);
        builder.append("ORDER BY ");
        String otherSort = DataUtil.safeToString(request.getSort(), "+updateAt");
        builder.append(SortingUtils.parseSorting("-type," + otherSort, UploadImages.class));
        builder.append("\n");
    }

    private UploadImagesDTO convertRow(Row row) {
        return UploadImagesDTO.builder()
                .id(DataUtil.safeToString(row.get("id")))
                .name(DataUtil.safeToString(row.get("name")))
                .type(DataUtil.safeToInt(row.get("type")))
                .path(DataUtil.safeToString(row.get("path")))
                .parentId(DataUtil.safeToString(row.get("parent_id")))
                .totalImages(DataUtil.safeToLong(row.get("total_images")))
                .previewImages(Arrays.stream(
                                DataUtil.safeToString(row.get("preview_images")).split(","))
                        .filter(url -> !DataUtil.isNullOrEmpty(url))
                        .limit(4)
                        .collect(Collectors.toList()))
                .updateAt((LocalDateTime) row.get("update_at"))
                .createAt((LocalDateTime) row.get("create_at"))
                .createBy(DataUtil.safeToString(row.get("create_by")))
                .build();
    }
}
