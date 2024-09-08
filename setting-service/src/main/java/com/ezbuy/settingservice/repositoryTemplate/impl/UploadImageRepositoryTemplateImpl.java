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
package com.ezbuy.settingservice.repositoryTemplate.impl;

import com.ezbuy.settingmodel.dto.UploadImagesDTO;
import com.ezbuy.settingmodel.dto.request.SearchImageRequest;
import com.ezbuy.settingmodel.model.UploadImages;
import com.ezbuy.settingservice.repositoryTemplate.UploadImageRepositoryTemplate;
import io.hoangtien2k3.commons.utils.DataUtil;
import io.hoangtien2k3.commons.utils.SQLUtils;
import io.hoangtien2k3.commons.utils.SortingUtils;
import io.r2dbc.spi.Row;
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
        query.append("limit :offset, :limit");
        params.put("limit", request.getPageSize());
        params.put("offset", (request.getPageIndex() - 1) * request.getPageSize());

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
                "select u.*, count(c.id) total_images, group_concat(c.path order by c.update_at desc) preview_images from upload_images u \n");
        builder.append("left join upload_images c on c.parent_id = u.id and c.status = 1 \n");
        builder.append("where u.status = 1 \n");

        if (!DataUtil.isNullOrEmpty(request.getFromDate())) {
            builder.append("and u.update_at >= :from \n");
            params.put("from", request.getFromDate().atStartOfDay());
        }

        if (!DataUtil.isNullOrEmpty(request.getToDate())) {
            builder.append("and u.update_at < :to \n");
            params.put("to", request.getToDate().plusDays(1).atStartOfDay());
        }

        if (!DataUtil.isNullOrEmpty(request.getName())) {
            builder.append("and lower(u.name) like concat('%', :name, '%') \n");
            params.put(
                    "name",
                    SQLUtils.replaceSpecialDigit(request.getName().trim().toLowerCase()));
        }

        builder.append("group by u.id, u.name, u.type, u.path, u.parent_id, u.status, \n"
                + "u.create_at, u.create_by, u.update_at, u.update_by \n");
        builder.append("order by ");
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
