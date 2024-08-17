package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.framework.utils.DataUtil;
import com.ezbuy.framework.utils.SortingUtils;
import com.ezbuy.settingmodel.dto.ContentSectionDetailDTO;
import com.ezbuy.settingmodel.dto.ContentSectionDTO;
import com.ezbuy.settingmodel.model.ContentSection;
import com.ezbuy.settingmodel.request.SearchContentSectionRequest;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ContentSectionRepositoryTemplateImpl extends BaseRepositoryTemplate implements ContentSectionRepositoryTemplate {

    private final R2dbcEntityTemplate template;

    @Override
    public Flux<ContentSectionDTO> queryList(SearchContentSectionRequest request) {
        String sorting;
        if (DataUtil.isNullOrEmpty(request.getSort())) {
            sorting = " update_at DESC \n";
        } else {
            sorting = SortingUtils.parseSorting(request.getSort(), ContentSection.class);
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        buildQuery(query, params, request);
        query.append("ORDER BY ").append(sorting).append(" \n");
        return listQuery(query.toString(), params, ContentSectionDTO.class);
    }

    @Override
    public Mono<Long> count(SearchContentSectionRequest request) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQuery(builder, params, request);
        return countQuery(builder.toString(), params);
    }

    @Override
    public Flux<ContentSectionDetailDTO> getDetailContentSection(String id) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        buildQueryGetDetail(query, params, id);
        DatabaseClient.GenericExecuteSpec exeSpec = template.getDatabaseClient().sql(query.toString());
        for (String key : params.keySet()) {
            exeSpec = exeSpec.bind(key, params.get(key));
        }
        return exeSpec.map((row, metadata) -> convertRow(row)).all();
    }

    private ContentSectionDetailDTO convertRow(Row row) {
        return ContentSectionDetailDTO.builder()
                .id(DataUtil.safeToString(row.get("id")))
                .sectionId(DataUtil.safeToString(row.get("section_id")))
                .type(DataUtil.safeToString(row.get("type")))
                .refId(DataUtil.safeToString(row.get("ref_id")))
                .refAlias(DataUtil.safeToString(row.get("ref_alias"))) // mapping refAlias de tra ve PYCXXX/LuongToanTrinhScontract
                .refType(DataUtil.safeToString(row.get("ref_type")))
                .name(DataUtil.safeToString(row.get("name")))
                .order(DataUtil.safeToLong(row.get("display_order")))
                .parentId(DataUtil.safeToString(row.get("parent_id")))
                .status(DataUtil.safeToString(row.get("status")))
                .sectionName(DataUtil.safeToString(row.get("market_section_name")))
                .build();
    }

    private void buildQueryGetDetail(StringBuilder builder, Map<String, Object> params, String id) {
        builder.append("select cs.id,cs.section_id,cs.name,cs.type,cs.ref_id,cs.ref_type,cs.status,cs.display_order, ms.name as market_section_name, cs.parent_id, cs.ref_alias ");
        builder.append("from content_section cs \n");
        builder.append("left join market_section ms on ms.id = cs.section_id where cs.status = 1 and cs.id =:id  \n");
        params.put("id", id);
    }

    private void buildQuery(StringBuilder builder, Map<String, Object> params, SearchContentSectionRequest request) {
        builder.append("select content_section.* from content_section \n");
        builder.append("where content_section.status = 1 \n");
        if (!DataUtil.isNullOrEmpty(request.getRefId())) {
            builder.append("and content_section.ref_id = :refId \n");
            params.put("refId", request.getRefId());
        }
        // bo sung alias cho PYCXXX/LuongToanTrinhScontract
        if (!DataUtil.isNullOrEmpty(request.getRefAlias())) {
            builder.append("and content_section.ref_alias = :refAlias \n");
            params.put("refAlias", request.getRefAlias());
        }
        if (!DataUtil.isNullOrEmpty(request.getType())) {
            builder.append("and content_section.type = :type \n");
            params.put("type", request.getType());
        }
        if (!DataUtil.isNullOrEmpty(request.getRefType())) {
            builder.append("and content_section.ref_type = :refType \n");
            params.put("refType", request.getRefType());
        }
    }

    private LocalDateTime getFromDate(LocalDate fromDate) {
        return fromDate.atTime(0, 0, 0);
    }

    private LocalDateTime getToDate(LocalDate toDate) {
        return toDate.atTime(23, 59, 59);
    }

}
